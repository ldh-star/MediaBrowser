package com.liangguo.mediaBrowser.interfaces


/**
 * @author ldh
 * 时间: 2022/1/5 16:53
 * 邮箱: 2637614077@qq.com
 */
interface IMediaViewerConfig {

    /**
     * 获取水平方向上最大拉扯阈值，超过就会触发隐藏
     */
    var hideThresholdHorizontal: Int

    /**
     * 获取竖直方向上最大拉扯阈值，超过就会触发隐藏
     */
    var hideThresholdVertical: Int

    /**
     * 竖直方向上拉扯的阻尼
     */
    var dampY: Float


    /**
     * 水平方向上拉扯的阻尼
     */
    var dampX: Float

    /**
     * 浏览媒体的背景颜色
     */
    var backgroundColor: Int

    /**
     * 浏览媒体的第二种背景颜色，用于点击过后作颜色切换
     */
    var backgroundColor2: Int


    /**
     * 缩放的倍率增强
     */
    var scaleEnhance: Float

    /**
     * 速度换算成像素单位
     * 速度 = v2px * px
     */
    var v2px: Int

    /**
     * 向下拉的最大速度，超过这个速度表示释放
     */
    var maxDownSpeed: Int

}