package com.example.wanandroid.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/28
 */
class WechatFragmentAdapter(
    private val fragmentActivity: Fragment,
    val fragments: ArrayList<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}