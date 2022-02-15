package com.example.wanandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.wanandroid.R

/**
 * description ： app内跳转网页
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
class WebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val address=intent.getStringExtra("address")?:""
        val mWebView:WebView=findViewById(R.id.webview)
        mWebView.settings.javaScriptEnabled=true
        mWebView.webViewClient= WebViewClient()
        mWebView.settings.domStorageEnabled=true
        mWebView.loadUrl(address)
    }
}