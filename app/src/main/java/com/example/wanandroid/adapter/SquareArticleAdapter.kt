package com.example.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Square

/**
 * description ： 广场的文章适配器
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
class SquareArticleAdapter (private val articleList:List<Square>)
    :RecyclerView.Adapter<SquareArticleAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val mTvAuthor:TextView=view.findViewById(R.id.square_rv_author)
        val mTvTitle:TextView=view.findViewById(R.id.square_rv_title)
        val mTvTime:TextView=view.findViewById(R.id.square_rv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.square_item,parent,false)
        val viewHolder=ViewHolder(view)
        //为recycleView的item注册点击事件
        viewHolder.itemView.setOnClickListener{

        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val square=articleList[position]
        holder.mTvAuthor.text=square.author
        holder.mTvTitle.text=square.title
        holder.mTvTime.text=square.time
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}