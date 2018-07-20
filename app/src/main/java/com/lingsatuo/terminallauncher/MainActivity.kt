package com.lingsatuo.terminallauncher

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lingsatuo.widget.XTextView
import android.view.WindowManager



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<XTextView>(R.id.title).text = "Terminal Launcher"
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun onBackPressed() {

        super.onBackPressed()
    }
}
