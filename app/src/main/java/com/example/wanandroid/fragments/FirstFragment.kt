package com.example.wanandroid.fragments

import android.content.Context
import android.net.Uri
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.alin.lib.bannerlib.listener.ImageLoaderInterface
import com.bumptech.glide.Glide
import com.example.wanandroid.R
import com.example.wanandroid.`class`.Home
import com.example.wanandroid.`class`.Square
import com.example.wanandroid.adapter.BannerAdapter
import com.example.wanandroid.adapter.HomeArticleAdapter
import com.example.wanandroid.adapter.SquareArticleAdapter
import com.example.wanandroid.bean.ArticleResponse
import com.example.wanandroid.bean.BannerBean
import com.example.wanandroid.bean.HomeArticleResponse
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.imageaware.ImageAware
import com.youth.banner.Banner
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttp
import okhttp3.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/23
 */
class FirstFragment() : Fragment() {
    private lateinit var layoutManager1: RecyclerView.LayoutManager
    private lateinit var adapter1: HomeArticleAdapter
    var curPage = 0
    var isLoading = false
    var lastPosition = 498
    lateinit var recycleView: RecyclerView
    lateinit var viewPager2: ViewPager2
    var listPath = ArrayList<com.example.wanandroid.`class`.Banner>()
    var listTitle = ArrayList<String>()
    var homeList = ArrayList<Home>()
    lateinit var view2: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        view2 = inflater.inflate(R.layout.fragment_first, container, false)
        viewPager2 = view2.findViewById(R.id.home_vp2)
        recycleView = view2.findViewById(R.id.home_rv)
        return view2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                //监听是否滑动
                lastPosition = position
            }
        })
        initRecycleView()
        loadMoreData()
        freshRecycleView()
    }

    private fun initRecycleView() {
        val prefs=activity?.getSharedPreferences("cookie",Context.MODE_PRIVATE)
        val cookie=prefs?.getString("cookie","")?:""
        activity?.runOnUiThread {
            layoutManager1 = LinearLayoutManager(context)
            adapter1 = HomeArticleAdapter(homeList,cookie)
            recycleView.run {
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

    private fun freshRecycleView() {
        var isSliding: Boolean = false
        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //dx为横向滚动 dy为竖向滚动
                //如果为竖向滚动,则isSliding属性为true，横向滚动则为false
                isSliding = dy > 0
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //manager必须为LinearLayoutManager
                val manager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
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
        val prefs=activity?.getSharedPreferences("cookie",Context.MODE_PRIVATE)
        val cookie=prefs?.getString("cookie","")?:""
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/article/list/$curPage/json", cookie,
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
        val articleResponse = gson.fromJson(responseData, HomeArticleResponse::class.java)
        val size = articleResponse.data.datas!!.size - 1
        for (i in 0..size) {
            val address = articleResponse.data.datas!![i].link
            val author = articleResponse.data.datas!![i].author
            val shareUser = articleResponse.data.datas!![i].shareUser
            val title = articleResponse.data.datas!![i].title
            val superName = articleResponse.data.datas!![i].superChapterName + " /"
            val name = articleResponse.data.datas!![i].chapterName
            val niceDate = articleResponse.data.datas!![i].niceDate
            val id = articleResponse.data.datas!![i].id
            val collect = articleResponse.data.datas!![i].collect
            if (author == "") {
                //添加新的Square
                homeList.add(
                    Home(
                        shareUser,
                        superName,
                        name,
                        title,
                        niceDate,
                        address, id, collect
                    )
                )
            } else {
                homeList.add(Home(author, superName, name, title, niceDate, address, id, collect))
            }
        }
        //不能重新创建一个adapter，这样会使得recycleView自动滚动到顶部，而应该使用原来的adapter
        freshRecycleViewData()
    }

    private fun freshRecycleViewData() {
        activity?.runOnUiThread {
            //在原来的adapter里刷新数据即可
            recycleView.adapter?.notifyDataSetChanged()
        }
    }

    private fun getData() {
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/banner/json", "",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val bannerResponse = gson.fromJson(responseData, BannerBean::class.java)
                    for (i in 0..2) {
                        listPath.add(com.example.wanandroid.`class`.Banner(bannerResponse.data[i].imagePath))
                    }
                    activity?.runOnUiThread {
                        viewPager2.adapter = BannerAdapter(listPath)
                        viewPager2.currentItem = 498
                    }
                    for (i in 0..1000) {
                        lastPosition++
                        Thread.sleep(5000)
                        activity?.runOnUiThread {
                            viewPager2.currentItem = lastPosition
                        }
                    }
                }
            })
    }

}