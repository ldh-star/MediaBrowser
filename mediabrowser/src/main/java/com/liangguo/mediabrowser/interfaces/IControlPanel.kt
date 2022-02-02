package com.liangguo.mediabrowser.interfaces

import android.view.View
import com.liangguo.mediabrowser.MediaBrowserView


/**
 * @author ldh
 * 时间: 2022/1/5 22:16
 * 邮箱: 2637614077@qq.com
 *
 * 浏览媒体的界面应该有一个信息面板，可以对当前媒体进行一些操作
 */
abstract class IControlPanel<Media> {

    /**
     * 持有我的MediaBrowserView
     */
    lateinit var mediaBrowserView: MediaBrowserView

    /**
     * 获取这个面板的View
     */
    abstract fun getView(): View

    /**
     * 当媒体被点击时
     */
    abstract fun onItemClick(position: Int, showPanel: Boolean)

    /**
     * 媒体改变时的回调
     */
    abstract fun onMediaChanged(position: Int, media: Media)

}