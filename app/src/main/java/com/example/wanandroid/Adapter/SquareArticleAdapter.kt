package com.example.wanandroid.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.RecycleViewAdapterClass.Square
import org.w3c.dom.Text

/**
 * description ： 广场的文章适配器
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
class SquareArticleAdapter (private val articleList:List<Square>)
    :RecyclerView.Adapter<SquareArticleAdapter.ViewHolder>(){
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val author:TextView=view.findViewById(R.id.square_rv_author)
        val title:TextView=view.findViewById(R.id.square_rv_title)
        val time:TextView=view.findViewById(R.id.square_rv_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.square_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val square=articleList[position]
        holder.author.text=square.author
        holder.title.text=square.title
        holder.time.text=square.time
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}