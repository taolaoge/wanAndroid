package com.example.wanandroid.Fragments.WechatFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.Adapter.WechatArticleAdapter
import com.example.wanandroid.Bean.ArticleResponse
import com.example.wanandroid.R
import com.example.wanandroid.RecycleViewAdapterClass.Wechat
import com.example.wanandroid.Utils.HttpUtil
import com.example.wanandroid.Utils.TimeUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/28
 */
class Fragment3:Fragment() {
    private lateinit var layoutManager1: RecyclerView.LayoutManager
    private lateinit var adapter1: WechatArticleAdapter
    var curPage = 1
    lateinit var view2: View
    lateinit var rv: RecyclerView
    private val wechatList = ArrayList<Wechat>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view2 = inflater.inflate(R.layout.fragment1, container, false)
        rv = view2.findViewById(R.id.wechat_rv_1)
        //必须在网络请求完成后更新ui，因为网络请求是异步的
        //如果在主线程更新，即有可能网络请求数据还未请求到就刷新ui
        initData()
        initRecycleView()
        freshRecycleView()
        return view2
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
                    if (lastVisibleItem == (totalItem - 1)) {
                        loadMoreData()
                    }
                }
            }

        })
    }

    private fun loadMoreData() {
        HttpUtil.sendOkHttpGetRequest("https://wanandroid.com/wxarticle/list/410/$curPage/json","",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    //网络请求成功后，解析json数据
                    curPage += 1
                    val responseData = response.body?.string()
                    recycleViewData(responseData)
                }
            })
    }


    private fun initData() {
        HttpUtil.sendOkHttpGetRequest("https://wanandroid.com/wxarticle/list/410/$curPage/json","",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    //网络请求成功后，解析json数据
                    curPage += 1
                    val responseData = response.body?.string()
                    recycleViewData(responseData)
                }
            })
    }


    private fun initRecycleView() {
        //子线程更新ui，先要获取activity才能调用runOnUiThread
        activity?.runOnUiThread {
            layoutManager1 = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
            adapter1 = WechatArticleAdapter(wechatList)
                rv.layoutManager = layoutManager1
                rv.addItemDecoration(
                    DividerItemDecoration(
                        context, DividerItemDecoration.VERTICAL
                    )
                )
                rv.adapter = adapter1


        }
    }

    private fun recycleViewData(responseData: String?) {
        val gson = Gson()
        val articleResponse = gson.fromJson(responseData, ArticleResponse::class.java)
        val size = articleResponse.data.total % 10 + 9
        //初次请求数据和其他请求数据项目数量不同
        if (curPage == 2) {
            for (i in 0..size) {
                val address=articleResponse.data.datas!![i].link
                val author = articleResponse.data.datas!![i].author
                val shareUser = articleResponse.data.datas!![i].shareUser
                val title = articleResponse.data.datas!![i].title
                val publishTime = articleResponse.data.datas!![i].publishTime
                val time= TimeUtil.timeStampToTime(publishTime)
                if (author == "") {
                    //添加新的Wechat
                    wechatList.add(Wechat(shareUser, title, time,address))
                } else {
                    wechatList.add(Wechat(author, title, time,address))
                }
            }
            initRecycleView()
        } else {
            for (i in 0..19) {
                val address=articleResponse.data.datas!![i].link
                val author = articleResponse.data.datas!![i].author
                val shareUser = articleResponse.data.datas!![i].shareUser
                val title = articleResponse.data.datas!![i].title
                val publishTime = articleResponse.data.datas!![i].publishTime
                val  time= TimeUtil.timeStampToTime(publishTime)
                if (author == "") {
                    //添加新的Square
                    wechatList.add(Wechat(shareUser, title, time,address))
                } else {
                    wechatList.add(Wechat(author, title, time,address))
                }
            }
            //不能重新创建一个adapter，这样会使得recycleView自动滚动到顶部，而应该使用原来的adapter
            freshRecycleViewData()
        }
    }

    private fun freshRecycleViewData() {
        activity?.runOnUiThread{
            //在原来的adapter里刷新数据即可
            rv.adapter!!.notifyDataSetChanged()
        }
    }
}