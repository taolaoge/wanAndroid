package com.example.wanandroid.adapter

import android.content.Intent
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Star
import com.example.wanandroid.activity.WebActivity
import com.example.wanandroid.utils.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import org.w3c.dom.Text
import java.io.IOException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/10
 */
class StarArticleAdapter(private val articleList: List<Star>,private val cookie:String):
RecyclerView.Adapter<StarArticleAdapter.ViewHolder>(){
    lateinit var parent1:ViewGroup
    lateinit var viewHolder:StarArticleAdapter.ViewHolder
    var mCheckBoxStates= SparseBooleanArray()
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val mTvAuthor:TextView=view.findViewById(R.id.star_rv_author)
        val mTvTime:TextView=view.findViewById(R.id.star_rv_time)
        val mTvTitle:TextView=view.findViewById(R.id.star_rv_title)
        val mTvName:TextView=view.findViewById(R.id.star_rv_name)
        val mCheckBox:CheckBox=view.findViewById(R.id.star_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.star_item, parent, false)
        viewHolder=ViewHolder(view)
        parent1=parent
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val star = articleList[position]
        holder.mTvAuthor.text = star.author
        holder.mTvTitle.text = star.title
        holder.mTvTime.text = star.time
        holder.mTvName.text = star.name
        val id = star.id
        val originId = star.originId
        holder.itemView.setOnClickListener {
            val address = star.address
            val intent = Intent(parent1.context, WebActivity::class.java)
            intent.putExtra("address", address)
            parent1.context.startActivity(intent)
        }
        holder.mCheckBox.isChecked = true
        //为CheckBox标记位置
        holder.mCheckBox.tag = position
        //为CheckBox注册点击事件
        holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            //当CheckBox被点击时，此时它的位置
            val pos: Int = buttonView.tag as Int
            if (cookie == "") {
                holder.mCheckBox.isChecked=false
                Toast.makeText(parent1.context, "请先登录", Toast.LENGTH_SHORT).show()
            } else {
                if (!isChecked) {
                    //如果没被选中即用户取消收藏，则将它的位置添加进SparseBooleanArray()
                    //第一个参数为键，第二个参数为添加的Boolean
                    mCheckBoxStates.put(pos, false)
                    //doSomething()，取消收藏文章
                    val requestBody = FormBody.Builder()
                        .add("originId", originId.toString())
                        .build()
                    HttpUtil.sendOkHttpPostRequest("https://www.wanandroid.com/lg/uncollect/$id/json",
                        requestBody,
                        cookie,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {

                            }

                            override fun onResponse(call: Call, response: Response) {
                                //收藏成功后的逻辑
                            }

                        })
                }
            }
        }
            //adapter加载数据时，这个位置的item取出它的值，如果无，则返回false，即CheckBox不会被选中
            //这一步是因为rv的复用导致可能CheckBox也会复用
            holder.mCheckBox.isChecked = mCheckBoxStates.get(position, true)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}