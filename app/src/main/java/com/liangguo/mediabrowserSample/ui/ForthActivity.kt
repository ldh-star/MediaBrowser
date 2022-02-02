package com.liangguo.mediabrowserSample.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.liangguo.mediabrowser.views.MediaBrowserPopupView
import com.liangguo.mediabrowserSample.R
import com.liangguo.mediabrowserSample.mediaBrowser.MyMediaLoader
import com.liangguo.mediabrowserSample.mediaBrowser.loadPageAdapter
import com.liangguo.mediabrowserSample.model.MediaBean
import com.liangguo.mediabrowserSample.model.medias
import com.liangguo.mediabrowserSample.widget.RecyclerAdapter2


/**
 * @author ldh
 * 时间: 2022/1/6 22:23
 * 邮箱: 2637614077@qq.com
 */
class ForthActivity: AppCompatActivity(), RecyclerAdapter2.OnItemClickListener {

    private val mRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val mRecyclerAdapter = RecyclerAdapter2(medias)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerAdapter.itemClickListener = this
    }

    override fun onItemClick(position: Int, mediaBean: MediaBean) {
        MediaBrowserPopupView.Builder<MediaBean>(this)
            .configMediaBrowser {
                //设置自定义的媒体集
                mediaList = medias
                //查看的起始位置，就是你点的这个item的项
                startIndex = position
                //查看的媒体换页了，这时要把recyclerView对应那一项的ImageView返回去，允许为null
                pageChangeListener = { currentPage ->
                    mRecyclerView.layoutManager?.findViewByPosition(
                        currentPage
                    )?.findViewById(R.id.imageView_item)
                }
                //自定义媒体加载器，用于SnapView的加载，加载你自己自定义的媒体实体
                mediaLoader = MyMediaLoader()
                //媒体加载器
                mediaPagerAdapter =
                    { container: ViewGroup, position: Int, media: MediaBean ->
                        loadPageAdapter(container, position, media)
                    }
            }
            //这里可以对Xpopup进行配置，使用这个需要在你自己的模块里配置Xpopup
            //在你自己模块里添加 implementation 'com.github.li-xiaojun:XPopup:2.6.7'
            .configXpopup {
                //这里可以设置Xpopup.Builder的所有参数
                hasBlurBg(true)
            }
            .show()

    }

}