package com.example.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Home

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/7
 */
class HomeArticleAdapter(private val data: List<Home>) :
    RecyclerView.Adapter<HomeArticleAdapter.InnerHolder>() {
    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvAuthor: TextView = view.findViewById(R.id.home_rv_author)
        val mTvTime: TextView = view.findViewById(R.id.home_rv_time)
        val mTvSuperName: TextView = view.findViewById(R.id.home_rv_super_name)
        val mTvName: TextView = view.findViewById(R.id.home_rv_name)
        val mTvTitle: TextView = view.findViewById(R.id.home_rv_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_item, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val home = data[position]
        holder.mTvAuthor.text=home.author
        holder.mTvTime.text=home.time
        holder.mTvName.text=home.name
        holder.mTvSuperName.text=home.superName
        holder.mTvTitle.text=home.title
    }

    override fun getItemCount(): Int {
        return data.size
    }
}