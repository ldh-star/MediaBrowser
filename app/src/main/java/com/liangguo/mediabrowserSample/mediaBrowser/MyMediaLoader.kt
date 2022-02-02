package com.liangguo.mediabrowserSample.mediaBrowser

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.liangguo.mediabrowser.interfaces.IMediaLoader
import com.liangguo.mediabrowserSample.model.MediaBean


/**
 * @author ldh
 * 时间: 2022/1/6 21:22
 * 邮箱: 2637614077@qq.com
 */
class MyMediaLoader: IMediaLoader<MediaBean> {
    override fun loadMedia(media: MediaBean, imageView: ImageView) {
        imageView.loadMedia(media)
    }
}


fun loadPageAdapter(container: ViewGroup, position: Int, media: MediaBean): View {
    val mediaView = ImageView(container.context)
    mediaView.loadMedia(media)
    return mediaView
}

fun ImageView.loadMedia(media: MediaBean) {
    if (media.resId == -1) {
        Glide.with(context)
            .load("https://i0.hdslb.com/bfs/sycp/creative_img/202201/453b6c11515e8be19b7f7bde30dfd831.jpg@672w_378h_1c.webp")
            .into(this)
    } else {
        setImageResource(media.resId)
    }
}