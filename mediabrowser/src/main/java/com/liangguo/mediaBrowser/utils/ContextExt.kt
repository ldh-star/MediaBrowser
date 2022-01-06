package com.liangguo.mediaBrowser.utils

import android.R
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import java.util.*


/**
 * @author ldh
 * 时间: 2022/1/5 18:04
 * 邮箱: 2637614077@qq.com
 */

fun Context.getActivityContentLeft(): Int {
    if (!isLandscape()) return 0
    //以Activity的content的left为准
    val decorView = (this as Activity).window.decorView.findViewById<View>(R.id.content)
    val loc = IntArray(2)
    decorView.getLocationInWindow(loc)
    return loc[0]
}

fun Context.isLandscape(): Boolean {
    return (this.resources.configuration.orientation
            == Configuration.ORIENTATION_LANDSCAPE)
}

fun Context.isLayoutRtl(): Boolean {
    val primaryLocale: Locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        resources.configuration.locale
    }
    return TextUtils.getLayoutDirectionFromLocale(primaryLocale) == View.LAYOUT_DIRECTION_RTL
}


fun Context.getAppWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return -1
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.x
}