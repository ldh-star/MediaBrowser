package com.liangguo.mediabrowserSample.bigview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.liangguo.mediabrowserSample.R


/**
 * @author liangdinghong
 * 时间: 2023/12/24 16:12
 * 邮箱: liangdinghong.ldh@alibaba-inc.com
 */
class BigImageActivity: AppCompatActivity() {

    private val imageView by lazy { findViewById<BigImageView>(R.id.image_view) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_big_image)
        kotlin.runCatching {
            imageView.setImage(assets.open("long_img.png"))
        }
    }

}