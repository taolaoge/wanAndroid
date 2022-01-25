package com.example.wanandroid.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.Adapter.SquareArticleAdapter
import com.example.wanandroid.Gson.ArticleResponse
import com.example.wanandroid.R
import com.example.wanandroid.RecycleViewAdapterClass.Square
import com.example.wanandroid.Utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/23
 */
class SquareFragment : Fragment() {
    lateinit var view2: View
    lateinit var rv:RecyclerView
    private val squareList = ArrayList<Square>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view2 = inflater.inflate(R.layout.fragment_square, container, false)
        rv=view2.findViewById(R.id.square_recycleView)
        //必须在网络请求完成后更新ui，因为网络请求是异步的
        //如果在主线程更新，即有可能网络请求数据还未请求到就刷新ui
        initData()
        return view2
    }

    private fun initData() {
        HttpUtil.sendOkHttpGetRequest("https://wanandroid.com/user_article/list/1/json",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    //网络请求成功后，解析json数据
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val articleResponse = gson.fromJson(responseData, ArticleResponse::class.java)
                    val size=articleResponse.data.size
                    for (i in 0..18) {
                        val author = articleResponse.data.datas!![i].author
                        val shareUser = articleResponse.data.datas!![i].shareUser
                        val title = articleResponse.data.datas!![i].title
                        val publishTime = articleResponse.data.datas!![i].publishTime
                        if (author == "") {
                            squareList.add(Square(shareUser, title, "0"))
                        } else {
                            squareList.add(Square(author, title, "0"))
                        }
                    }
                    initView()
                }
            })
    }


    private fun initView() {
        //子线程更新ui，先要获取activity才能调用runOnUiThread
        activity?.runOnUiThread{
            val layoutManager1 = LinearLayoutManager(context)
            val adapter1 = SquareArticleAdapter(squareList)
            rv.run {
                layoutManager=layoutManager1
                addItemDecoration(
                    DividerItemDecoration(
                        context, DividerItemDecoration.VERTICAL
                    )
                )
                adapter = adapter1
            }

        }
    }


}