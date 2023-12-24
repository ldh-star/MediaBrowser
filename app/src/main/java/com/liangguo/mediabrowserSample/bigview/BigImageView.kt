package com.liangguo.mediabrowserSample.bigview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import java.io.InputStream


/**
 * @author liangdinghong
 * 时间: 2023/12/24 15:24
 * 邮箱: liangdinghong.ldh@alibaba-inc.com
 *
 * 支持大图加载的ImageView
 */
class BigImageView : View, GestureDetector.OnGestureListener, View.OnTouchListener {

    private val rect = Rect()

    /** 用来做内存复用的工具 */
    private val options = BitmapFactory.Options()

    /** 手势支持辅助类 */
    private val gestureDetector by lazy { GestureDetector(context, this) }

    /** 滚动辅助类 */
    private val scroller by lazy { Scroller(context) }

    private var imageWidth = 0

    private var imageHeight = 0

    /** 区域解码器 */
    private var decoder: BitmapRegionDecoder? = null

    private var bitmap: Bitmap? = null

    private var viewWidth = 0

    private var viewHeight = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setOnTouchListener(this)
    }

    /** 设置图片的input流 */
    fun setImage(inputStream: InputStream) {
        // 第一步，获取图片的宽和高
        // 通过仅加载边缘的方式来获取，避免把整张图片加载进内存再获取宽高
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, options)
        imageWidth = options.outWidth
        imageHeight = options.outHeight
        // 开启内存复用
        options.inMutable = true
        // 设置图片格式为rgb565，节省内存
        options.inPreferredConfig = Bitmap.Config.RGB_565
        // 这个属性用完后一定要记得改回false
        options.inJustDecodeBounds = false

        // 区域解码
        decoder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            BitmapRegionDecoder.newInstance(inputStream)
        } else {
            BitmapRegionDecoder.newInstance(inputStream, false)
        }
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewWidth = measuredWidth
        viewHeight = measuredHeight
        // 确定图片的加载区域
        rect.top = 0
        rect.left = 0
        rect.right = imageWidth
        rect.bottom = viewHeight

    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        decoder?.let { decoder ->
            // 内存复用，option里提供了一块bitmap对象，每次加载的时候用option提供的对象去加载
            options.inBitmap = bitmap
            bitmap = decoder.decodeRegion(rect, options)
            // 图片加载进View的时候要根据View的尺寸和Bitmap的尺寸进行缩放
            val scale = viewWidth.toFloat() / imageWidth
            val matrix = Matrix()
            matrix.setScale(scale, scale)
            bitmap?.let {
                canvas.drawBitmap(it, matrix, null)
            }
        }
    }


    override fun onDown(p0: MotionEvent?): Boolean {
        // 如果还在惯性滑动，则手指按下时应该停止滑动
        if (!scroller.isFinished) {
            scroller.forceFinished(true)
        }
        // 接收后续事件
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(
        event1: MotionEvent?,
        event2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        // e1开始事件  e2即时事件
        rect.apply {
            // 上下滚动时，改变Rect的显示区域
            offset(0, distanceY.toInt())
            // 判断到顶和到底的情况
            if (bottom > imageHeight) {
                bottom = imageHeight
                top = imageHeight - viewHeight
            }
            if (top < 0) {
                top = 0
                bottom = viewHeight
            }
//            if (right > imageWidth) {
//                right = imageWidth
//                left = imageWidth - viewWidth
//            }
//            if (left < 0) {
//                left = 0
//                right = viewWidth
//            }
        }
        invalidate()
        return false
    }


    override fun onFling(
        p0: MotionEvent?,
        p1: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        scroller.fling(
//            rect.left,
            0,
            rect.top,
//            velocityX.toInt(),
            0,
            -velocityY.toInt(),
            0,
//            imageWidth - viewWidth,
            0,
            0,
            imageHeight - viewHeight
        )
        return false
    }

    override fun computeScroll() {
        super.computeScroll()
        if (!scroller.isFinished && scroller.computeScrollOffset()) {
//            rect.left = scroller.currX
//            rect.right = rect.left + imageWidth - viewWidth
            rect.top = scroller.currY
            rect.bottom = rect.top + imageHeight - viewHeight
        }
    }

    override fun onLongPress(p0: MotionEvent?) {}

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(p1)
    }

}