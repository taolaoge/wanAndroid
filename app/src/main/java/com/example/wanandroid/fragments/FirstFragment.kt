package com.example.wanandroid.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.alin.lib.bannerlib.listener.ImageLoaderInterface
import com.bumptech.glide.Glide
import com.example.wanandroid.R
import com.example.wanandroid.adapter.BannerAdapter
import com.example.wanandroid.bean.ArticleResponse
import com.example.wanandroid.bean.BannerBean
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.imageaware.ImageAware
import com.youth.banner.Banner
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttp
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/23
 */
class FirstFragment() : Fragment() {
    lateinit var viewPager2: ViewPager2
    var listPath = ArrayList<com.example.wanandroid.`class`.Banner>()
    var listTitle = ArrayList<String>()
    lateinit var view2: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view2 = inflater.inflate(R.layout.fragment_first, container, false)
        viewPager2 = view2.findViewById(R.id.home_vp2)
        return view2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/banner/json", "",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val bannerResponse = gson.fromJson(responseData, BannerBean::class.java)
                    for (i in 0..2) {
                        listPath.add(com.example.wanandroid.`class`.Banner(bannerResponse.data[i].imagePath))
                    }
                    activity?.runOnUiThread {
                        viewPager2.adapter = BannerAdapter(listPath)
                        viewPager2.currentItem = 498
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
    }

}