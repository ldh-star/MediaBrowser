package com.liangguo.mediaBrowser.config

import android.widget.ImageView
import com.liangguo.mediaBrowser.interfaces.*
import com.liangguo.mediaBrowser.views.Background


/**
 * @author ldh
 * 时间: 2022/1/5 15:07
 * 邮箱: 2637614077@qq.com
 *
 * @param Media 你自己定义的媒体类实体
 */
class MediaBrowserConfig<Media : Any> {

    /**
     * 用来做为动画的ImageView，由MediaBrowserView自己实现
     */
    internal lateinit var snapView: ImageView

    /**
     * 媒体浏览器打开之后的初始显示的位置
     */
    var startIndex = 0

    /**
     * 媒体集
     */
    var mediaList = mutableListOf<Media>()

    /**
     * 当用户在浏览换页的时候，会回调这个，这个时候要传回需要绑定动画的View
     */
    var pageChangeListener: PageChangedListener = { _ -> null }

    /**
     * 媒体加载器，务必要传入
     */
    lateinit var mediaLoader: IMediaLoader<Media>


    /**
     * 点击后就返回，默认为false
     */
    var clickToBack = false

    /**
     * 动画的ImageView的默认ScaleType
     * 当有srcView且srcView为ImageView的时候就用srcView的scaleType
     * 没有的时候就使用默认的scaleType
     */
    var defaultScaleType = ImageView.ScaleType.FIT_CENTER

    /**
     * 动画时长，默认300ms
     */
    var animDuration = 300L

    /**
     * 为SnapView加载媒体
     */
    fun loadSnapImage(position: Int) {
        mediaLoader.loadMedia(mediaList[position], snapView)
    }

    /**
     * 控制面板，显示在图片的上方
     */
    internal val controlPanels = mutableListOf<IControlPanel<Media>>()

    /**
     * 背景的View，用来控制alpha和背景颜色
     */
    internal val backgroundViews = mutableListOf<Background>()

    fun add(background: Background) {
        backgroundViews.add(background)
    }

    fun add(controlPanel: IControlPanel<Media>) {
        controlPanels.add(controlPanel)
    }

    /**
     * 通知所有控制面板媒体更新了
     */
    fun notifyPanelMediaChanged(position: Int) {
        controlPanels.forEach { it.onMediaChanged(position, mediaList[position]) }
    }

    /**
     * 你可以通过这个自定义适配器，不过需要你自己去写
     * 媒体加载，scaleType之类的逻辑，最后返回要添加进来的view就行
     */
    var mediaPagerAdapter: IMediaPagerAdapter<Media>? = null

    /**
     * 单击过后面板状态会改变
     */
    var panelVisibleChangedCallback: PanelVisibleChangedCallback = {}


    /**
     * 状态改变的回调
     * true表示是显示
     * false表示是关闭
     */
    var stateChangedCallback: StateChangedCallback = {}

}