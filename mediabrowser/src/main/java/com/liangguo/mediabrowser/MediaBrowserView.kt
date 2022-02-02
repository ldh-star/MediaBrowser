package com.liangguo.mediabrowser

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.transition.*
import androidx.viewpager.widget.ViewPager
import com.liangguo.mediabrowser.config.MediaBrowserConfig
import com.liangguo.mediabrowser.interfaces.IMediaDragCallback
import com.liangguo.mediabrowser.interfaces.PopupInterface
import com.liangguo.mediabrowser.interfaces.SingleParamCallBack
import com.liangguo.mediabrowser.utils.*
import com.liangguo.mediabrowser.views.MediaViewer
import com.liangguo.mediabrowser.widget.MediaPagerAdapter


/**
 * @author ldh
 * 时间: 2022/1/5 15:03
 * 邮箱: 2637614077@qq.com
 */
class MediaBrowserView : FrameLayout, PopupInterface, ViewPager.OnPageChangeListener,
    IMediaDragCallback, SingleParamCallBack<Int> {

    private lateinit var mConfig: MediaBrowserConfig<out Any>

    /**
     * 当前的状态是否显示控制面板，默认不显示
     */
    private var showPanel = false

    /**
     * 源view，用来显示动画，因为显示动画时需要获取原view的参数
     */
    private var mSrcView: View? = null

    /**
     * 关闭的回调，这个时候你应该去关闭其父的Dialog或者PopupView
     */
    var dismissCallback = {}

    private val mediaViewer = MediaViewer(context).also {
        it.viewPager.addOnPageChangeListener(this)
        it.iMediaDragCallback = this
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        addView(mediaViewer)
    }

    fun <T : Any> config(func: MediaBrowserConfig<T>.() -> Unit) = run {
        val config = MediaBrowserConfig<T>().also { it.snapView = ImageView(context) }
        config.func()
        this.mConfig = config

        //为ViewPager配置Adapter
        mediaViewer.viewPager.adapter =
            MediaPagerAdapter(config.mediaList).also {
                it.mediaLoader = config.mediaLoader
                it.onItemClickListener = this@MediaBrowserView
                it.mediaPagerAdapter = config.mediaPagerAdapter
            }
        mediaViewer.viewPager.setCurrentItem(config.startIndex, false)
        mediaViewer.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mediaViewer.viewPager.visibility = View.INVISIBLE
        config.snapView.visibility = View.INVISIBLE
        config.loadSnapImage(mediaViewer.viewPager.currentItem)
        //设置背景view
        config.backgroundViews.forEach { addView(it, 0) }
        //把控制面板添加进去
        config.controlPanels.forEach {
            addView(it.getView())
            it.mediaBrowserView = this
        }


        //这里有一个细节，由于ViewPager如果给他设置切换到第一页，而他本身就在第一页，这个时候不会触发PageChangedCallback，所以如果是去设置到第一页，需要手动通知一下
        if (config.startIndex == 0) {
            onPageSelected(0)
        }

        this
    }


    /**
     * 当显示的时候，这个时候应该开启过渡动画
     */
    override fun onShow() {
        //为背景启动显示的动画
        mConfig.backgroundViews.forEach { it.startAlphaAnimation(true, mConfig.animDuration) }

        mConfig.snapView.let { animView ->

            //设置SnapView的起始状态
            val rect = getSrcViewRect()
            animView.translationX = rect.left.toFloat()
            animView.translationY = rect.top.toFloat()
            animView.layoutParams = LayoutParams(rect.width(), rect.height())

            //为animView设置scaleType
            if (mSrcView != null && mSrcView is ImageView) {
                //应优先和srcView的scaleType保持一致
                animView.scaleType = (mSrcView as ImageView).scaleType
            } else {
                //否则才使用默认的
                animView.scaleType = mConfig.defaultScaleType
            }

            //显示出SnapView
            animView.visibility = VISIBLE
            mediaViewer.addView(animView)

            //去做动画
            animView.post {
                mediaViewer.isTransationing = true

                TransitionManager.beginDelayedTransition(
                    mediaViewer, TransitionSet()
                        .setDuration(mConfig.animDuration)
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeTransform())
                        .addTransition(ChangeImageTransform())
                        .setInterpolator(DecelerateInterpolator(2f))
                        .addListener(object : TransitionListenerAdapter() {
                            override fun onTransitionEnd(transition: Transition) {
                                mediaViewer.isTransationing = false
                                //动画过程中parent的子view都被移除了，所以这里要重新添加ViewPager
                                mediaViewer.addView(mediaViewer.viewPager)
                                mediaViewer.viewPager.visibility = View.VISIBLE
                                //并且去除掉SnapView
                                mediaViewer.removeView(animView)
                            }
                        })
                )

                //设置SnapView的结束状态
                animView.scaleType = ImageView.ScaleType.FIT_CENTER
                animView.translationX = 0f
                animView.translationY = 0f
                animView.layoutParams = LayoutParams(mediaViewer.width, mediaViewer.height)

                //为background设置它应有的颜色
                mConfig.backgroundViews.forEach { it.setBackgroundColor(if (showPanel) it.colorB else it.colorA) }

            }
        }

    }


    /**
     * 调用该方法触发关闭动画
     */
    override fun onDismiss() {
        //为背景启动关闭的动画
        mConfig.backgroundViews.forEach { it.startAlphaAnimation(false, mConfig.animDuration) }
        //重新加载SnapView显示的图片
        mConfig.loadSnapImage(mediaViewer.viewPager.currentItem)

        mConfig.snapView.let { animView ->
            //设置SnapView的起始状态
            animView.translationX = mediaViewer.viewPager.x
            animView.translationY = mediaViewer.viewPager.y
            animView.layoutParams =
                LayoutParams(mediaViewer.viewPager.width, mediaViewer.viewPager.height)
            animView.scaleX = mediaViewer.viewPager.scaleX
            animView.scaleY = mediaViewer.viewPager.scaleY
            //显示出SnapView
            animView.visibility = VISIBLE

            //将SnapView添加进来
            mediaViewer.addView(mConfig.snapView)

            //将ViewPager设为不可见
            mediaViewer.viewPager.visibility = View.INVISIBLE

            //去做动画
            animView.post {
                mediaViewer.isTransationing = true
                TransitionManager.beginDelayedTransition(
                    mediaViewer, TransitionSet()
                        .setDuration(mConfig.animDuration)
                        .addTransition(ChangeBounds())
                        .addTransition(ChangeTransform())
                        .addTransition(ChangeImageTransform())
                        .setInterpolator(DecelerateInterpolator(2f))
                        .addListener(object : TransitionListenerAdapter() {
                            override fun onTransitionEnd(transition: Transition) {
                                mediaViewer.isTransationing = false
                            }
                        })
                )

                //设置SnapView的结束状态
                if (mSrcView != null && mSrcView is ImageView) {
                    //应优先和srcView的scaleType保持一致
                    animView.scaleType = (mSrcView as ImageView).scaleType
                } else {
                    //否则才使用默认的
                    animView.scaleType = mConfig.defaultScaleType
                }

                val rect = getSrcViewRect()
                animView.layoutParams = LayoutParams(rect.width(), rect.height())
                animView.translationX = rect.left.toFloat()
                animView.translationY = rect.top.toFloat()
                animView.scaleX = 1f
                animView.scaleY = 1f

                //离开的时候要让原srcView变得可见
                mSrcView?.startAlphaAnimation(true, mConfig.animDuration)
                mSrcView?.visibility = View.VISIBLE

            }
        }

    }


    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        //让上一个srcView可见
        mSrcView?.visibility = View.VISIBLE

        mSrcView = mConfig.pageChangeListener.invoke(position)
        //让现在新的srcView不可见
        mSrcView?.startAlphaAnimation(false, mConfig.animDuration)
        mSrcView?.visibility = View.INVISIBLE

        mConfig.notifyPanelMediaChanged(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }


    /**
     * 这个矩形是SrcView的位置，用来做过渡动画
     * 如果srcView为空，则返回的rect也为空
     */
    private fun getSrcViewRect(): Rect {
        //默认就是充满容器的位置尺寸
        var rect = Rect(width / 2, height / 2, width / 2, height / 2)
        mSrcView?.let { srcView ->
            val locations = IntArray(2)
            srcView.getLocationInWindow(locations)
            var left = locations[0] - context.getActivityContentLeft()
            if (context.isLayoutRtl()) {
                left = -(context.getAppWidth() - locations[0] - srcView.width)
                rect = Rect(left, locations[1], left + srcView.width, locations[1] + srcView.height)
            } else {
                rect = Rect(left, locations[1], left + srcView.width, locations[1] + srcView.height)
            }
        }
        return rect
    }

    override fun onRelease() {
        dismissCallback.invoke()
    }

    override fun onDrag(scale: Float) {
        mConfig.backgroundViews.forEach { it.alpha = scale }
        mConfig.controlPanels.forEach {
            it.getView().alpha = scale
        }
    }

    /**
     * 当点击了媒体的item的回调
     */
    override fun callBack(param: Int) {
        if (mConfig.clickToBack) {
            //单击则直接返回
            dismissCallback.invoke()
            return
        }
        //向控制面板发送点击的回调
        mConfig.controlPanels.forEach { it.onItemClick(param, showPanel) }
        //让背景的颜色改变
        mConfig.backgroundViews.forEach { background ->
            if (showPanel) {
                background.startColorAnimation(background.colorB, background.colorA)
            } else {
                background.startColorAnimation(background.colorA, background.colorB)
            }
        }
        mConfig.panelVisibleChangedCallback.invoke(showPanel)

        showPanel = !showPanel
    }

}