package com.example.wanandroid.adapter

import android.content.Intent
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Item
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
 * date : 2022/2/11
 */
class ItemArticleAdapter(private val articleList:List<Item>,val cookie:String):
RecyclerView.Adapter<ItemArticleAdapter.ViewHolder>(){
    lateinit var viewHolder:ItemArticleAdapter.ViewHolder
    var mCheckBoxStates= SparseBooleanArray()
    lateinit var parent1:ViewGroup
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val mTvTime:TextView=view.findViewById(R.id.item_rv_time)
        val mTvTitle:TextView=view.findViewById(R.id.item_rv_title)
        val mTvDesc:TextView=view.findViewById(R.id.item_rv_desc)
        val mTvAuthor:TextView=view.findViewById(R.id.item_rv_author)
        val mImg:ImageView=view.findViewById(R.id.item_rv_img)
        val mCheckBox:CheckBox=view.findViewById(R.id.item_rv_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_item, parent, false)
        viewHolder=ViewHolder(view)
        parent1=parent
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=articleList[position]
        val envelopePic=item.envelopPic
        val author=item.author
        val time=item.niceData
        val title=item.title
        val desc=item.desc
        holder.mTvAuthor.text=author
        holder.mTvDesc.text=desc
        holder.mTvTime.text=time
        holder.mTvTitle.text=title
        Glide.with(holder.itemView.context).load(envelopePic).into(holder.mImg)
        holder.itemView.setOnClickListener {
            val address=item.link
            val intent= Intent(parent1.context, WebActivity::class.java)
            intent.putExtra("address",address)
            parent1.context.startActivity(intent)
        }
        val id=item.id
        //为CheckBox标记位置
        holder.mCheckBox.tag=position
        //为CheckBox注册点击事件,CheckBox被点击时回调
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
        if (item.collect) {
            holder.mCheckBox.isChecked = true
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}