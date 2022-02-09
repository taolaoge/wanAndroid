package com.example.wanandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import com.example.wanandroid.R

class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val address=intent.getStringExtra("address")?:""
        val mWebView:WebView=findViewById(R.id.webview)
        mWebView.loadUrl(address)
    }
}