package com.liangguo.mediabrowser.views

import android.content.Context
import com.liangguo.mediabrowser.MediaBrowserView
import com.liangguo.mediabrowser.R
import com.liangguo.mediabrowser.config.MediaBrowserConfig
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView


/**
 * @author ldh
 * 时间: 2022/1/6 0:03
 * 邮箱: 2637614077@qq.com
 *
 * 基于Xpopup的PopupView显示图片查看
 */
class MediaBrowserPopupView<Media : Any>(context: Context) : BasePopupView(context) {

    private val mMediaBrowserView by lazy {
        popupContentView as MediaBrowserView
    }

    lateinit var config: MediaBrowserConfig<Media>.() -> Unit

    override fun getInnerLayoutId() = R.layout.inflate_popup_media_browser

    override fun onCreate() {
        super.onCreate()
        initView()
    }

    private fun initView() {
        mMediaBrowserView.dismissCallback = {
            dismiss()
        }
        try {
            mMediaBrowserView.config(config)
        } catch (e: Exception) {
            throw IllegalArgumentException("你应该先配置好config()。  $e")
        }
    }

    override fun doShowAnimation() {
        super.doShowAnimation()
        mMediaBrowserView.onShow()
    }

    override fun doDismissAnimation() {
        super.doDismissAnimation()
        mMediaBrowserView.onDismiss()
    }

    class Builder<Media : Any>(context: Context) {

        private val mMediaBrowserPopupView = MediaBrowserPopupView<Media>(context)

        private val mXPopupBuilder = XPopup.Builder(context)
            .isDestroyOnDismiss(true)
            .isLightStatusBar(true)

        /**
         * XpopupBuilder可以在这里配置
         */
        fun configXpopup(block: XPopup.Builder.() -> Unit) = run {
            block.invoke(mXPopupBuilder)
            this
        }

        /**
         * 对MediaBrowser进行配置
         */
        fun configMediaBrowser(config: MediaBrowserConfig<Media>.() -> Unit) = run {
            mMediaBrowserPopupView.config = config
            this
        }

        fun show() = run {
            mXPopupBuilder.asCustom(mMediaBrowserPopupView).show()
            this
        }

    }

}