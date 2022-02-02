package com.liangguo.mediabrowserSample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liangguo.mediabrowser.views.Background
import com.liangguo.mediabrowser.views.MediaBrowserPopupView
import com.liangguo.mediabrowserSample.R
import com.liangguo.mediabrowserSample.mediaBrowser.MyMediaLoader
import com.liangguo.mediabrowserSample.mediaBrowser.loadPageAdapter
import com.liangguo.mediabrowserSample.model.MediaBean
import com.liangguo.mediabrowserSample.model.medias
import com.liangguo.mediabrowserSample.widget.RecyclerAdapter2

class SecondActivity : AppCompatActivity(), RecyclerAdapter2.OnItemClickListener {

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
                //这里是背景组件
                add(Background(this@SecondActivity) {
                    //可以自己配置两种状态不同的背景色，默认是白色和黑色
                    colorA = getColor(R.color.purple_500)
                    colorB = getColor(R.color.teal_200)
                })

                //媒体加载器
                mediaPagerAdapter =
                    { container: ViewGroup, position: Int, media: MediaBean ->
                        loadPageAdapter(container, position, media)
                    }
            }.create().show()

    }

}