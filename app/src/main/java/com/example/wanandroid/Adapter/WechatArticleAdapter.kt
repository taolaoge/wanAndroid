package com.example.wanandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.RecycleViewAdapterClass.Square
import com.example.wanandroid.RecycleViewAdapterClass.Wechat
import org.w3c.dom.Text

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/30
 */
class WechatArticleAdapter(private val articleList: List<Wechat>) :
    RecyclerView.Adapter<WechatArticleAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val author: TextView = view.findViewById(R.id.wechat_rv_author)
        val title: TextView = view.findViewById(R.id.wechat_rv_title)
        val time: TextView = view.findViewById(R.id.wechat_rv_time)
        val position: TextView = view.findViewById(R.id.wechat_position_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wechat_item, parent, false)
        val viewHolder = ViewHolder(view)
        //为recycleView的item注册点击事件
        viewHolder.itemView.setOnClickListener {

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wechat = articleList[position]
        holder.author.text = wechat.author
        holder.title.text = wechat.title
        holder.time.text = wechat.time
        holder.position.text = wechat.author
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}