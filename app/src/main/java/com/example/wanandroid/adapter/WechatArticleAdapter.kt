package com.example.wanandroid.adapter

import android.content.Intent
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Wechat
import com.example.wanandroid.activity.WebActivity
import com.example.wanandroid.utils.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/30
 */
class WechatArticleAdapter(private val articleList: List<Wechat>,private val cookie:String) :
    RecyclerView.Adapter<WechatArticleAdapter.ViewHolder>(){
    var id=0
    lateinit var parent1:ViewGroup
    lateinit var viewHolder:WechatArticleAdapter.ViewHolder
    var mCheckBoxStates= SparseBooleanArray()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvAuthor: TextView = view.findViewById(R.id.wechat_rv_author)
        val mTvTitle: TextView = view.findViewById(R.id.wechat_rv_title)
        val mTvTime: TextView = view.findViewById(R.id.wechat_rv_time)
        val mTvPosition: TextView = view.findViewById(R.id.wechat_position_author)
        val mCheckBox:CheckBox=view.findViewById(R.id.wechat_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wechat_item, parent, false)
        viewHolder = ViewHolder(view)
        //为recycleView的item注册点击事件
        parent1=parent
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wechat = articleList[position]
        holder.mTvAuthor.text = wechat.author
        holder.mTvTitle.text = wechat.title
        holder.mTvTime.text = wechat.time
        holder.mTvPosition.text = wechat.author
        holder.itemView.setOnClickListener {
            val address=wechat.address
            val intent= Intent(parent1.context, WebActivity::class.java)
            intent.putExtra("address",address)
            parent1.context.startActivity(intent)
        }
        val id = wechat.id
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
                if (isChecked) {
                    //如果被选中，则将它的位置添加进SparseBooleanArray()
                    //第一个参数为键，第二个参数为添加的Boolean
                    mCheckBoxStates.put(pos, true)
                    //doSomething()，收藏文章
                    val requestBody = FormBody.Builder()
                        .build()
                    HttpUtil.sendOkHttpPostRequest("https://www.wanandroid.com/lg/collect/$id/json",
                        requestBody,
                        cookie,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {

                            }

                            override fun onResponse(call: Call, response: Response) {
                                //收藏成功后的逻辑
                            }

                        })
                } else {
                    //如果没有被选中，则删除键值对
                    mCheckBoxStates.delete(pos)
                    //doSomething()，取消收藏文章
                    val requestBody = FormBody.Builder()
                        .build()
                    HttpUtil.sendOkHttpPostRequest("https://www.wanandroid.com/lg/uncollect_originId/$id/json\n",
                        requestBody,
                        cookie,
                        object : Callback {
                            override fun onFailure(call: Call, e: IOException) {

                            }

                            override fun onResponse(call: Call, response: Response) {
                                //取消收藏成功后的逻辑
                            }

                        })
                }
            }
        }
            //adapter加载数据时，这个位置的item取出它的值，如果无，则返回false，即CheckBox不会被选中
            //这一步是因为rv的复用导致可能CheckBox也会复用
            holder.mCheckBox.isChecked = mCheckBoxStates.get(position, false)
            //后端的数据文章是否被收藏，上一行的mCheckBoxStates无法保存到本地，所以加载数据时是否收藏还需请求后端数据
            if (wechat.collect) {
                holder.mCheckBox.isChecked = true
            }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}