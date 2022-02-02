package com.liangguo.mediabrowser.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import androidx.viewpager.widget.ViewPager
import com.liangguo.mediabrowser.config.DefaultMediaViewerConfig
import com.liangguo.mediabrowser.interfaces.IMediaDragCallback
import com.liangguo.mediabrowser.interfaces.IMediaViewerConfig
import kotlin.math.abs

/**
 * @author ldh
 * 时间: 2022/1/5 16:49
 * 邮箱: 2637614077@qq.com
 *
 * 这个类包含了两个View，一个是ViewPager用来显示和滑动
 * 另一个是一个ImageView，用来在做动画的时候显示图片
 */
class MediaViewer : FrameLayout {

    /**
     * 用来浏览媒体的ViewPager
     */
    val viewPager = ViewPager(context)


    /**
     * 变化中... 打开或者关闭
     */
    var isTransationing = false

    private val mMediaViewerConfig: IMediaViewerConfig = DefaultMediaViewerConfig(this)

    var iMediaDragCallback: IMediaDragCallback? = null


    private val mDragHelper by lazy {
        ViewDragHelper.create(this, mDragHelperCallback)
    }

    private val mDragHelperCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {

        /**
         * 对触摸view判断，如果需要当前触摸的子View进行拖拽移动就返回true，否则返回false
         */
        override fun tryCaptureView(child: View, pointerId: Int) = !isTransationing

        /**
         * 返回拖拽子View在相应方向上可以被拖动的最远距离，默认为0
         */
        override fun getViewVerticalDragRange(child: View) =
            if (viewPager == child) viewPager.height else 0

        override fun getViewHorizontalDragRange(child: View) =
            if (viewPager == child) viewPager.width else 0

        /**
         * 拖拽的子View在所属方向上移动的位置，child为拖拽的子View，left为子view应该到达的x坐标，dx为挪动差值
         */
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return (viewPager.top + dy / mMediaViewerConfig.dampY).toInt()
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return (viewPager.left + dx / mMediaViewerConfig.dampX).toInt()
        }

        /**
         * 被拖拽的View位置变化时回调，changedView为位置变化的view，left、top变化后的x、y坐标，dx、dy为新位置与旧位置的偏移量
         */
        override fun onViewPositionChanged(
            changedView: View,
            left: Int,
            top: Int,
            dx: Int,
            dy: Int
        ) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (changedView !== viewPager) viewPager.offsetTopAndBottom(dy)
            val pageScale =
                1f - (abs(viewPager.top.toFloat()) / viewPager.height.toFloat()) * mMediaViewerConfig.dampY * mMediaViewerConfig.scaleEnhance

            viewPager.scaleX = pageScale
            viewPager.scaleY = pageScale
            changedView.scaleX = pageScale
            changedView.scaleY = pageScale
            iMediaDragCallback?.onDrag(pageScale)
        }

        /**
         * 当前拖拽的view松手或者ACTION_CANCEL时调用
         * 判断是否释放的逻辑：
         * y方向上拉扯的距离 + y方向上的速度 是否大于最大阈值
         * 或者 x 同理
         * @param xvel 离开屏幕时的速率
         * @param yvel 离开屏幕时的速率
         */
        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
            Log.e("速度", yvel.toString())
            if (abs(releasedChild.top + yvel / mMediaViewerConfig.v2px) > mMediaViewerConfig.hideThresholdVertical
                || abs(releasedChild.left + xvel / mMediaViewerConfig.v2px) > mMediaViewerConfig.hideThresholdHorizontal
                || yvel > mMediaViewerConfig.maxDownSpeed
            ) {
                iMediaDragCallback?.onRelease()
            } else {
                mDragHelper.smoothSlideViewTo(releasedChild, 0, 0)
                ViewCompat.postInvalidateOnAnimation(this@MediaViewer)

            }
        }

    }


    /**
     * 重写addView方法，使其能够安全的添加子view
     */
    override fun addView(view: View, index: Int) {
        view.parent?.let {
            if (it == this) return
            (it as ViewGroup).removeView(view)
        }
        super.addView(view, index)
    }

    /**
     * 重写addView方法，使其能够安全的添加子view
     */
    override fun addView(view: View) {
        view.parent?.let {
            if (it == this) return
            (it as ViewGroup).removeView(view)
        }
        super.addView(view)
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        viewPager.layoutParams = params
        addView(viewPager)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isTransationing = false
    }


    private var isVerticalDrag = false

    private var touchX = 0f

    private var touchY = 0f

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) return super.dispatchTouchEvent(ev)
        try {
            //报错信息：
            //Ignoring pointerId=-1 because ACTION_DOWN was not received for this pointer before ACTION_MOVE. It likely happened because  ViewDragHelper did not receive all the events in the event stream.

            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    touchX = ev.x
                    touchY = ev.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = ev.x - touchX
                    val dy = ev.y - touchY
                    viewPager.dispatchTouchEvent(ev)
                    isVerticalDrag = abs(dy) > abs(dx)
                    touchX = ev.x
                    touchY = ev.y
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    touchX = 0f
                    touchY = 0f
                    isVerticalDrag = false
                }
            }
        } catch (e: Exception) {
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val result: Boolean = mDragHelper.shouldInterceptTouchEvent(ev)
        if (ev.pointerCount > 1 && ev.action == MotionEvent.ACTION_MOVE) return false
//        return if (isTopOrBottomEnd() && isVerticalDrag) true else result && isVerticalDrag
        return isVerticalDrag && result
    }

    override fun computeScroll() {
        super.computeScroll()
        if (mDragHelper.continueSettling(false)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.pointerCount > 1) return false
        try {
            mDragHelper.processTouchEvent(ev)
            return true
        } catch (e: Exception) {
        }
        return true
    }

}