package com.liangguo.mediabrowserSample.mediaBrowser

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.liangguo.mediabrowser.interfaces.IMediaLoader
import com.liangguo.mediabrowserSample.model.MediaBean


/**
 * @author ldh
 * 时间: 2022/1/6 21:22
 * 邮箱: 2637614077@qq.com
 */
class MyMediaLoader: IMediaLoader<MediaBean> {
    override fun loadMedia(media: MediaBean, imageView: ImageView) {
        imageView.setImageResource(media.resId)
    }
}


fun loadPageAdapter(container: ViewGroup, position: Int, media: MediaBean): View {
    val mediaView = ImageView(container.context)
    mediaView.setImageResource(media.resId)
    return mediaView
}