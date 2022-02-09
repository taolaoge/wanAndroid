package com.example.wanandroid.adapter

import android.os.Parcel
import android.os.Parcelable
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
import com.example.wanandroid.`class`.Square
import com.example.wanandroid.fragments.SquareFragment
import com.example.wanandroid.utils.HttpUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException

/**
 * description ： 广场的文章适配器
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
class SquareArticleAdapter(private val articleList: List<Square>, private val cookie: String) :
    RecyclerView.Adapter<SquareArticleAdapter.ViewHolder>(){
    lateinit var viewHolder:SquareArticleAdapter.ViewHolder
    lateinit var parent1: ViewGroup
    var mCheckBoxStates=SparseBooleanArray()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mTvAuthor: TextView = view.findViewById(R.id.square_rv_author)
        val mTvTitle: TextView = view.findViewById(R.id.square_rv_title)
        val mTvTime: TextView = view.findViewById(R.id.square_rv_time)
        val mCheckBox: CheckBox = view.findViewById(R.id.square_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.square_item, parent, false)
        viewHolder = ViewHolder(view)
        //为recycleView的item(任意控件)注册点击事件
        parent1 = parent
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val square = articleList[position]
        holder.mTvAuthor.text = square.author
        holder.mTvTitle.text = square.title
        holder.mTvTime.text = square.time
        val id=square.id
        if(square.collect){
            holder.mCheckBox.isChecked=true
        }
        holder.mCheckBox.tag=position
        holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val pos:Int=buttonView.tag as Int
            if (isChecked){
                mCheckBoxStates.put(pos,true)
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
            }else{
                mCheckBoxStates.delete(pos)
            }
        }
        holder.mCheckBox.isChecked=mCheckBoxStates.get(position,false)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}