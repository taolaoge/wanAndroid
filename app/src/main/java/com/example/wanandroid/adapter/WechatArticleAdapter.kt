package com.example.wanandroid.adapter

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
    RecyclerView.Adapter<WechatArticleAdapter.ViewHolder>(),CompoundButton.OnCheckedChangeListener {
    var id=0
    lateinit var parent1:ViewGroup
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
        val viewHolder = ViewHolder(view)
        //为recycleView的item注册点击事件
        viewHolder.mCheckBox.setOnCheckedChangeListener(this)
        parent1=parent
        return viewHolder
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        if (isChecked) {
            if (cookie == "") {
                Toast.makeText(parent1.context, "请登录", Toast.LENGTH_SHORT).show()
            } else {
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
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val wechat = articleList[position]
        holder.mTvAuthor.text = wechat.author
        holder.mTvTitle.text = wechat.title
        holder.mTvTime.text = wechat.time
        holder.mTvPosition.text = wechat.author
        id=wechat.id
        if (wechat.collect){
            holder.mCheckBox.isChecked=true
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}