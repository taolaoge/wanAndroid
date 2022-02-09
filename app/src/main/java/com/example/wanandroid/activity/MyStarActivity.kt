package com.example.wanandroid.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Square
import com.example.wanandroid.`class`.Star
import com.example.wanandroid.adapter.SquareArticleAdapter
import com.example.wanandroid.adapter.StarArticleAdapter
import com.example.wanandroid.bean.ArticleResponse
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MyStarActivity : AppCompatActivity() {
    var curPage = 0
    lateinit var rv: RecyclerView
    var isLoading = false
    private var starList = ArrayList<Star>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_star)
        val toolbar: Toolbar = findViewById(R.id.star_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        rv = findViewById(R.id.my_star_rv)
        initRecycleView()
        loadMoreData()
        freshRecycleView()
    }


    private fun initRecycleView() {
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie = prefs?.getString("cookie", "") ?: ""
        val layoutManager1 = LinearLayoutManager(this)
        val adapter1 = StarArticleAdapter(starList, cookie)
        rv.layoutManager = layoutManager1
        rv.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL
            )
        )
        rv.adapter = adapter1
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
                        isLoading = true
                        loadMoreData()
                    }
                }
            }
        })
    }

    private fun loadMoreData() {
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie = prefs?.getString("cookie", "") ?: ""
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/lg/collect/list/$curPage/json",
            cookie,
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


    private fun recycleViewData(responseData: String?) {
        val gson = Gson()
        val articleResponse = gson.fromJson(responseData, ArticleResponse::class.java)
        val size = articleResponse.data.datas!!.size - 1
        for (i in 0..size) {
            val address = articleResponse.data.datas!![i].link
            val author = articleResponse.data.datas!![i].author
            val title = articleResponse.data.datas!![i].title
            val shareUser=articleResponse.data.datas!![i].shareUser
            val name = articleResponse.data.datas!![i].chapterName ?: ""
            val niceData = articleResponse.data.datas!![i].niceDate
            val id = articleResponse.data.datas!![i].id
            val originId = articleResponse.data.datas!![i].originId
            //添加新的Star
            starList.add(Star("匿名", title, name, niceData, address, id, originId))
        }
        //不能重新创建一个adapter，这样会使得recycleView自动滚动到顶部，而应该使用原来的adapter
        freshRecycleViewData()
    }

    private fun freshRecycleViewData() {
        runOnUiThread {
            rv.adapter?.notifyDataSetChanged()
        }
        //在原来的adapter里刷新数据即可
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //点击toolbar的返回按钮 finish此活动
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}