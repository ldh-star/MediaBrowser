package com.liangguo.mediabrowserSample.mediaBrowser

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toolbar
import com.liangguo.mediabrowser.interfaces.IControlPanel
import com.liangguo.mediabrowserSample.R
import com.liangguo.mediabrowserSample.model.MediaBean


/**
 * @author ldh
 * 时间: 2022/1/6 21:59
 * 邮箱: 2637614077@qq.com
 */
@SuppressLint("InflateParams")
class MyControlPanel(private val context: Context) : IControlPanel<MediaBean>() {

    private val mRootView by lazy {
        LayoutInflater.from(context).inflate(R.layout.inflate_control_panel, null)
    }

    private val mToolbar by lazy {
        getView().findViewById<Toolbar>(R.id.toolbar)
    }

    private var mCurrentMedia: MediaBean? = null

    override fun getView(): View = mRootView

    override fun onMediaChanged(position: Int, media: MediaBean) {
        mCurrentMedia = media
        mToolbar.title = position.toString()
    }


    override fun onItemClick(position: Int, showPanel: Boolean) {
        if (showPanel) {
            getView().visibility = View.GONE
        } else {
            getView().visibility = View.VISIBLE
        }
    }

    init {
        mToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        mToolbar.setNavigationOnClickListener { mediaBrowserView.dismissCallback.invoke() }
    }

}