package com.liangguo.mediaBrowser.widget

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.liangguo.mediaBrowser.interfaces.IMediaLoader
import com.liangguo.mediaBrowser.interfaces.IMediaPagerAdapter
import com.liangguo.mediaBrowser.interfaces.SingleParamCallBack


/**
 * @author ldh
 * 时间: 2021/12/23 15:21
 * 邮箱: 2637614077@qq.com
 */

class MediaPagerAdapter<T : Any>(var medias: List<T>): PagerAdapter() {

    var mediaLoader: IMediaLoader<T>? = null

    var onItemClickListener: SingleParamCallBack<Int>? = null

    /**
     * 你可以通过这个自定义适配器，不过需要你自己去写
     * 媒体加载，scaleType之类的逻辑，最后返回要添加进来的view就行
     */
    var mediaPagerAdapter: IMediaPagerAdapter<T>? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        mediaPagerAdapter?.let {
            val view = it.invoke(container, position, medias[position])
            view.setOnClickListener {
                onItemClickListener?.callBack(position)
            }
            container.addView(view)
            return view
        } ?: let {
            val imageView = ImageView(container.context)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            mediaLoader?.loadMedia(medias[position], imageView)
            container.addView(imageView)
            imageView.setOnClickListener {
                onItemClickListener?.callBack(position)
            }
            return imageView
        }
    }

    override fun getCount(): Int {
        return medias.size
    }



    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view === obj
    }

}
