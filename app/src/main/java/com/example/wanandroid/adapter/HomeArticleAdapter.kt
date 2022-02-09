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
import com.example.wanandroid.`class`.Home
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
 * date : 2022/2/7
 */
class HomeArticleAdapter(private val data: List<Home>,private val cookie:String) :
    RecyclerView.Adapter<HomeArticleAdapter.InnerHolder>(),CompoundButton.OnCheckedChangeListener {
    var id:Int=0
    lateinit var parent1:ViewGroup
    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvAuthor: TextView = view.findViewById(R.id.home_rv_author)
        val mTvTime: TextView = view.findViewById(R.id.home_rv_time)
        val mTvSuperName: TextView = view.findViewById(R.id.home_rv_super_name)
        val mTvName: TextView = view.findViewById(R.id.home_rv_name)
        val mTvTitle: TextView = view.findViewById(R.id.home_rv_title)
        val mCheckBox:CheckBox=view.findViewById(R.id.home_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_item, parent, false)
        val holder=InnerHolder(view)
        holder.mCheckBox.setOnCheckedChangeListener(this)
        parent1=parent
        return InnerHolder(view)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val home = data[position]
        holder.mTvAuthor.text=home.author
        holder.mTvTime.text=home.time
        holder.mTvName.text=home.name
        holder.mTvSuperName.text=home.superName
        holder.mTvTitle.text=home.title
        id=home.id
        if (home.collect){
            holder.mCheckBox.isChecked=true
        }
    }

    override fun getItemCount(): Int {
        return data.size
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
}