package com.liangguo.mediaBrowser.config

import com.liangguo.mediaBrowser.interfaces.IMediaViewerConfig
import com.liangguo.mediaBrowser.views.MediaViewer


/**
 * @author ldh
 * 时间: 2022/1/5 16:53
 * 邮箱: 2637614077@qq.com
 */
class DefaultMediaViewerConfig(private val mediaViewer: MediaViewer) : IMediaViewerConfig {

    override var hideThresholdHorizontal = dip2px(30f)

    override var hideThresholdVertical = dip2px(70f)

    override var dampY = 1.4f

    override var dampX = 1.2f

    override var scaleEnhance = 1.5f

    override var v2px = 700

    override var backgroundColor = 0xFFFFFFFF.toInt()

    override var backgroundColor2 = 0

    override var maxDownSpeed = 2000

    private fun dip2px(dpValue: Float): Int {
        val scale: Float = mediaViewer.context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

}