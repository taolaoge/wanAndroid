package com.example.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Banner
import com.example.wanandroid.fragments.FirstFragment

/**
 * description ： 首页banner图适配器
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/5
 */
class BannerAdapter(private val data: List<Banner>) :
    RecyclerView.Adapter<BannerAdapter.InnerHolder>() {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView:ImageView = view.findViewById(R.id.home_vp_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_vp2_item, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val newPosition=position%3
        Glide.with(holder.itemView.context).load(data[newPosition].uri).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }
}