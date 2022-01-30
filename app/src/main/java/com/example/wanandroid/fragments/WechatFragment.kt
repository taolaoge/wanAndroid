package com.example.wanandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.wanandroid.adapter.WechatFragmentAdapter
import com.example.wanandroid.fragments.wechat.*
import com.example.wanandroid.R
import com.google.android.material.tabs.TabLayout

import com.google.android.material.tabs.TabLayoutMediator


/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/23
 */
class WechatFragment : Fragment() {
    private lateinit var view1: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1 = inflater.inflate(R.layout.fragment_wechat, container, false)
        initViewPager()
        return view1
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = view1.findViewById(R.id.wechat_viewpager2)
        val tabLayout: TabLayout = view1.findViewById(R.id.wechat_tabLayout)
        var fragments = ArrayList<Fragment>()
        fragments.add(Fragment1())
        fragments.add(Fragment2())
        fragments.add(Fragment3())
        fragments.add(Fragment4())
        fragments.add(Fragment5())
        fragments.add(Fragment6())
        fragments.add(Fragment7())
        fragments.add(Fragment8())
        fragments.add(Fragment9())
        fragments.add(Fragment10())
        fragments.add(Fragment11())
        fragments.add(Fragment12())
        fragments.add(Fragment13())
        fragments.add(Fragment14())
        val data = arrayOf(
            "鸿洋",
            "郭霖",
            "玉刚说",
            "承香墨影",
            "Android群英传",
            "code小生",
            "谷歌开发者",
            "奇卓社",
            "美团技术团队",
            "GcsSloop",
            "互联网侦察",
            "susion随心",
            "程序亦非猿",
            "Gityuan"
        )
        val adapter1 = WechatFragmentAdapter(this, fragments)
        viewPager.adapter = adapter1
        TabLayoutMediator(
            tabLayout, viewPager
        ) { tab, position ->
            tab.text = data[position]
        }.attach()
    }
}