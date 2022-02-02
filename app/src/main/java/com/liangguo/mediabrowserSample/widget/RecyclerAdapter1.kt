package com.liangguo.mediabrowserSample.widget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.liangguo.mediabrowserSample.R
import com.liangguo.mediabrowserSample.model.MediaBean


/**
 * @author ldh
 * 时间: 2022/1/5 15:29
 * 邮箱: 2637614077@qq.com
 */
class RecyclerAdapter1(private val mMedias: MutableList<MediaBean>) : RecyclerView.Adapter<RecyclerAdapter1.ViewHolder>() {

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int, mediaBean: MediaBean)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image_a, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(mMedias[position].resId)
        holder.imageView.setOnClickListener { itemClickListener?.onItemClick(position, mMedias[position]) }
    }

    override fun getItemCount() = mMedias.size

}