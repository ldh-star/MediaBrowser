package com.liangguo.mediabrowserSample.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.liangguo.mediabrowser.views.MediaBrowserPopupView
import com.liangguo.mediabrowserSample.R
import com.liangguo.mediabrowserSample.widget.RecyclerAdapter1
import com.liangguo.mediabrowserSample.mediaBrowser.MyMediaLoader
import com.liangguo.mediabrowserSample.mediaBrowser.loadPageAdapter
import com.liangguo.mediabrowserSample.model.MediaBean
import com.liangguo.mediabrowserSample.model.medias

class FirstActivity : AppCompatActivity(), RecyclerAdapter1.OnItemClickListener {

    private val mRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recyclerView)
    }

    private val mRecyclerAdapter = RecyclerAdapter1(medias)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        mRecyclerView.adapter = mRecyclerAdapter
        mRecyclerAdapter.itemClickListener = this
    }


    override fun onItemClick(position: Int, mediaBean: MediaBean) {
        //核心代码如下，在item的点击事件里调用
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
                //轻点一下返回
                clickToBack = true
                //媒体加载器
                mediaPagerAdapter =
                    { container: ViewGroup, position: Int, media: MediaBean ->
                        loadPageAdapter(container, position, media)
                    }
            }.create().show()

    }


}