package com.liangguo.mediabrowser.interfaces


/**
 * @author ldh
 * 时间: 2022/1/5 16:51
 * 邮箱: 2637614077@qq.com
 *
 * 当用户大图浏览媒体时，拖拽用的回调
 */
interface IMediaDragCallback {

    /**
     * 拖拽超过一定比例，将退出大图浏览
     */
    fun onRelease()

    /**
     * 拖拽过程中的回调
     * @param scale 浏览的View的scale
     */
    fun onDrag(scale: Float)

}