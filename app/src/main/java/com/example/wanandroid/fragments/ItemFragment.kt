package com.example.wanandroid.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.wanandroid.R
import com.example.wanandroid.adapter.WechatFragmentAdapter
import com.example.wanandroid.fragments.Item.Fragment1
import com.example.wanandroid.fragments.Item.Fragment10
import com.example.wanandroid.fragments.Item.Fragment11
import com.example.wanandroid.fragments.Item.Fragment12
import com.example.wanandroid.fragments.Item.Fragment13
import com.example.wanandroid.fragments.Item.Fragment2
import com.example.wanandroid.fragments.Item.Fragment3
import com.example.wanandroid.fragments.Item.Fragment4
import com.example.wanandroid.fragments.Item.Fragment5
import com.example.wanandroid.fragments.Item.Fragment6
import com.example.wanandroid.fragments.Item.Fragment7
import com.example.wanandroid.fragments.Item.Fragment8
import com.example.wanandroid.fragments.Item.Fragment9
import com.example.wanandroid.fragments.wechat.*
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/23
 */
class ItemFragment:Fragment() {
    lateinit var view1:View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view1=layoutInflater.inflate(R.layout.fragment_item,container,false)
        return view1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = view1.findViewById(R.id.item_viewpager2)
        val tabLayout: TabLayout = view1.findViewById(R.id.item_tabLayout)
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
            "完整项目",
            "跨平台应用",
            "资源聚合类",
            "动画",
            "RV列表动效",
            "项目基础功能",
            "网络&文件下载",
            "TextView",
            "键盘",
            "快应用",
            "日历&时钟",
            "K线图",
            "硬件相关",
            "表格类"
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