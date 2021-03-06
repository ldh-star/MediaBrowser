package com.liangguo.mediabrowserSample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.liangguo.mediabrowserSample.ui.FirstActivity
import com.liangguo.mediabrowserSample.ui.ForthActivity
import com.liangguo.mediabrowserSample.ui.SecondActivity
import com.liangguo.mediabrowserSample.ui.ThirdActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button1).setOnClickListener(this)
        findViewById<View>(R.id.button2).setOnClickListener(this)
        findViewById<View>(R.id.button3).setOnClickListener(this)
        findViewById<View>(R.id.button4).setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button1 -> startActivity(Intent(this, FirstActivity::class.java))
            R.id.button2 -> startActivity(Intent(this, SecondActivity::class.java))
            R.id.button3 -> startActivity(Intent(this, ThirdActivity::class.java))
            R.id.button4 -> startActivity(Intent(this, ForthActivity::class.java))
        }
    }

}