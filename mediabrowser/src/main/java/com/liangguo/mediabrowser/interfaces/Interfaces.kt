package com.liangguo.mediabrowser.interfaces

import android.view.View
import android.view.ViewGroup


/**
 * @author ldh
 * 时间: 2022/1/5 16:13
 * 邮箱: 2637614077@qq.com
 */

/**
 * 当用户在浏览换页的时候，会回调这个，这个时候要传回需要绑定动画的View
 */
typealias PageChangedListener = (position: Int) -> View?

/**
 * 关闭的回调
 */
typealias DismissCallback = () -> Unit

/**
 * 你可以通过这个自定义适配器，不过需要你自己去写
 * 媒体加载，scaleType之类的逻辑，最后返回要添加进来的view就行
 */
typealias IMediaPagerAdapter<Media> = (container: ViewGroup, position: Int, media: Media) -> View

/**
 * 单击过后面板状态会改变
 */
typealias PanelVisibleChangedCallback = (showPanel: Boolean) -> Unit

/**
 * 状态改变的回调
 * true表示是显示
 * false表示是关闭
 */
typealias StateChangedCallback = (show: Boolean) -> Unit