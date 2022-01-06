package com.liangguo.mediaBrowser.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AlphaAnimation


/**
 * @author ldh
 * 时间: 2022/1/5 23:00
 * 邮箱: 2637614077@qq.com
 */


fun View.startColorAnimation(colorA: Int, colorB: Int) {
    //创建动画,这里的关键就是使用ArgbEvaluator, 后面2个参数就是 开始的颜色,和结束的颜色.
    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorA, colorB)
    colorAnimator.addUpdateListener { animation ->
        val color = animation.animatedValue as Int //之后就可以得到动画的颜色了
        setBackgroundColor(color) //设置一下, 就可以看到效果.
    }
    colorAnimator.duration = 250
    colorAnimator.start()
}

/**
 * 为View执行alpha动画
 */
fun View.startAlphaAnimation(show: Boolean, duration: Long) {
    val anim = if (show) AlphaAnimation(0f, 1f) else AlphaAnimation(1f, 0f)
    anim.duration = duration
    startAnimation(anim)
}
