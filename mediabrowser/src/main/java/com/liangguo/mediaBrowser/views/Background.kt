package com.liangguo.mediaBrowser.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup


/**
 * @author ldh
 * 时间: 2022/1/5 22:21
 * 邮箱: 2637614077@qq.com
 */
@SuppressLint("ViewConstructor")
class Background(context: Context, func: Background.() -> Unit) : View(context) {

    /**
     * 第一种状态的颜色，默认纯黑
     */
    var colorA = 0xFF000000.toInt()

    /**
     * 第二种状态的颜色，默认纯白
     */
    var colorB = 0xFFFFFFFF.toInt()

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        func.invoke(this)
    }

}