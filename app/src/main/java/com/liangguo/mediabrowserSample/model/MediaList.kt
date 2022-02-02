package com.liangguo.mediabrowserSample.model

import com.liangguo.mediabrowserSample.R


/**
 * @author ldh
 * 时间: 2022/1/6 20:55
 * 邮箱: 2637614077@qq.com
 */

private val mMedias = mutableListOf(
    MediaBean(R.drawable.img_a),
    MediaBean(R.drawable.img_b),
    MediaBean(R.drawable.img_c),
    MediaBean(R.drawable.img_d),
    MediaBean(R.drawable.img_e),
    MediaBean(R.drawable.img_f),
    MediaBean(R.drawable.img_g),
)

var medias = mutableListOf<MediaBean>().also {
    for (i in 1..50) {
        it.add(mMedias.random())
    }
}
