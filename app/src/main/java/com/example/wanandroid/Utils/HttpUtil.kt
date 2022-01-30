package com.example.wanandroid.Utils

import okhttp3.Cookie
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
object HttpUtil {
    fun sendOkHttpGetRequest(address:String,cookie:String, callback: okhttp3.Callback){
        val client= OkHttpClient()
        val request= Request.Builder().url(address).addHeader("Cookie",cookie).build()
        client.newCall(request).enqueue(callback)
    }

    fun sendOkHttpPostRequest(address: String, requestBody: RequestBody, callback:okhttp3.Callback){
        val client=OkHttpClient()
        val request=Request.Builder().url(address).post(requestBody).build()
        client.newCall(request).enqueue(callback)
    }
}