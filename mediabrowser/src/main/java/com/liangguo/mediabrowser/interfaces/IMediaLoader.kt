package com.liangguo.mediabrowser.interfaces

import android.widget.ImageView


/**
 * @author ldh
 * 时间: 2022/1/5 16:20
 * 邮箱: 2637614077@qq.com
 *
 * 媒体加载器，这个类需要调用方自己去实现，把media显示在对应的ImageView上
 */
interface IMediaLoader<Media : Any> {

    /**
     * 调用方自己去实现在ImageView上显示出media
     * @param media 调用方自己定义的实体
     * @param imageView 需要去显示的ImageView
     */
    fun loadMedia(media: Media, imageView: ImageView)

}