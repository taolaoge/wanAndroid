package com.example.wanandroid.fragments.Item

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Item
import com.example.wanandroid.adapter.ItemArticleAdapter
import com.example.wanandroid.bean.ArticleResponse
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/11
 */
class Fragment5 : Fragment() {
    private lateinit var layoutManager1: RecyclerView.LayoutManager
    private lateinit var adapter1: ItemArticleAdapter
    var curPage = 1
    var isLoading = false
    lateinit var rv: RecyclerView
    lateinit var view2: View
    private val itemList = ArrayList<Item>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view2=inflater.inflate(R.layout.fragment1,container,false)
        rv=view2.findViewById(R.id.wechat_rv)
        return view2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        loadMoreData()
        freshRecycleView()
    }

    private fun freshRecycleView() {
        var isSliding: Boolean = false
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx为横向滚动 dy为竖向滚动
                //如果为竖向滚动,则isSliding属性为true，横向滚动则为false
                isSliding = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //manager必须为LinearLayoutManager
                val manager: LinearLayoutManager = rv.layoutManager as LinearLayoutManager
                //newState是RecycleView的状态 如果它的状态为没有滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    val lastVisibleItem = manager.findLastCompletelyVisibleItemPosition()
                    val totalItem = manager.itemCount
                    if (lastVisibleItem == (totalItem - 1) && !isLoading) {
                        isLoading=true
                        loadMoreData()
                    }
                }
            }

        })
    }

    private fun loadMoreData() {
        val prefs=activity?.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie=prefs?.getString("cookie","")?:""
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/project/list/1/json?cid=314", cookie,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    isLoading = false
                }

                override fun onResponse(call: Call, response: Response) {
                    //网络请求成功后，解析json数据
                    curPage += 1
                    val responseData = response.body?.string()
                    recycleViewData(responseData)
                    isLoading = false
                }
            })
    }


    private fun initRecycleView() {
        val prefs=activity?.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie=prefs?.getString("cookie","")?:""
        activity?.runOnUiThread {
            layoutManager1 = LinearLayoutManager(context)
            adapter1 = ItemArticleAdapter(itemList,cookie)
            rv.run {
                layoutManager = layoutManager1
                addItemDecoration(
                    DividerItemDecoration(
                        context, DividerItemDecoration.VERTICAL
                    )
                )
                adapter = adapter1
            }
        }
    }
    private fun freshRecycleViewData() {
        activity?.runOnUiThread {
            //在原来的adapter里刷新数据即可
            rv.adapter?.notifyDataSetChanged()
        }
    }

    private fun recycleViewData(responseData: String?) {
        val gson = Gson()
        val articleResponse = gson.fromJson(responseData, ArticleResponse::class.java)
        val size = articleResponse.data.datas!!.size - 1
        for (i in 0..size) {
            val envelopPic=articleResponse.data.datas!![i].envelopePic as String
            val address = articleResponse.data.datas!![i].link
            val author = articleResponse.data.datas!![i].author
            val shareUser = articleResponse.data.datas!![i].shareUser
            val title = articleResponse.data.datas!![i].title
            val publishTime = articleResponse.data.datas!![i].publishTime
            val time = articleResponse.data.datas!![i].niceDate
            val id=articleResponse.data.datas!![i].id
            val desc=articleResponse.data.datas!![i].desc as String
            val collect=articleResponse.data.datas!![i].collect
            if (author == "") {
                //添加新的Square
                itemList.add(Item(author,collect,desc, envelopPic,address, time,title,id))
            } else {
                itemList.add(Item(shareUser,collect,desc, envelopPic,address, time,title,id))
            }
        }
        //不能重新创建一个adapter，这样会使得recycleView自动滚动到顶部，而应该使用原来的adapter
        freshRecycleViewData()
    }
}