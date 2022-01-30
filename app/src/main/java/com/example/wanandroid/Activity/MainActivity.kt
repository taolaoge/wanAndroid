package com.example.wanandroid.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wanandroid.Bean.LoginResponse
import com.example.wanandroid.Fragments.*
import com.example.wanandroid.R
import com.example.wanandroid.Utils.HttpUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import org.w3c.dom.Text
import java.io.IOException

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var lastIndex = 0
    lateinit var navView: NavigationView
    lateinit var headerLayout: View
    lateinit var mTvLevel: TextView
    lateinit var mTvRank: TextView
    lateinit var mTvUsername: TextView
    lateinit var fragment0: Fragment
    lateinit var fragment1: Fragment
    lateinit var fragment2: Fragment
    lateinit var fragment3: Fragment
    lateinit var fragment4: Fragment
    private var fragments = arrayListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        initSharedPreferences()
        initView()
        initData()
        initBottomNavigation()
        initNavigation()
    }


    private fun initView() {
        navView = findViewById(R.id.navView)
        headerLayout = navView.inflateHeaderView(R.layout.nav_header)
        mTvUsername = headerLayout.findViewById(R.id.nav_tv_go)
        mTvLevel = headerLayout.findViewById(R.id.nav_lever)
        mTvRank = headerLayout.findViewById(R.id.nav_rank)
        mTvUsername.setOnClickListener(this)
    }

    private fun initSharedPreferences() {
        val prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        val username: String = prefs.getString("name", "") ?: ""
        val password: String = prefs.getString("password", "") ?: ""
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        HttpUtil.sendOkHttpPostRequest(
            "https://www.wanandroid.com/user/login",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val loginResponse = gson.fromJson(responseData, LoginResponse::class.java)
                    val message = loginResponse.errorMsg
                    if (message == "") {
                        mTvUsername.text = username
                    }
                }
            })

    }

    private fun initNavigation() {
        val navigationView: NavigationView = findViewById(R.id.navView)
        navigationView.setCheckedItem(R.id.nav_tv_go)
    }

    private fun initBottomNavigation() {
        val title: TextView = findViewById(R.id.toolbar_title)
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        //设置监听器 切换fragment
        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_first -> {
                    setFragmentPosition(0)
                    title.text = "玩Android"
                }
                R.id.bottom_square -> {
                    setFragmentPosition(1)
                    title.text = "广场"
                }
                R.id.bottom_wechat -> {
                    setFragmentPosition(2)
                    title.text = "公众号"
                }
                R.id.bottom_system -> {
                    setFragmentPosition(3)
                    title.text = "体系"
                }
                R.id.bottom_item -> {
                    setFragmentPosition(4)
                    title.text = "项目"
                }
            }
            true
        })


    }

    private fun initData() {
        fragment0 = FirstFragment()
        fragment1 = SquareFragment()
        fragment2 = WechatFragment()
        fragment3 = SystemFragment()
        fragment4 = ItemFragment()
        fragments?.run {
            add(fragment0)
            add(fragment1)
            add(fragment2)
            add(fragment3)
            add(fragment4)
        }
        setFragmentPosition(0)
        val title: TextView = findViewById(R.id.toolbar_title)
        title.text = "玩Android"

    }

    private fun setFragmentPosition(position: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val currentFragment = fragments[position]
        val lastFragment = fragments[lastIndex]
        lastIndex = position
        //隐藏上一个fragment
        transaction.hide(lastFragment)
        //如果这个currentFragment没有被添加进去，则开一个新的transaction先移除这个Fragment
        //再原来的transaction内加入这个fragment
        if (!currentFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(currentFragment).commit()
            //将currentFragment添加到我们的一个占位的容器内
            //第一个参数为容器，第二个参数为我们想要添加的fragment
            transaction.add(R.id.lin_lay_fragment, currentFragment)

        }
        transaction.show(currentFragment)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerlayout)
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nav_tv_go -> {
                //判断是否已经登录
                if (mTvUsername.text == "去登陆") {
                    val intent = Intent(this, LoginActivity::class.java)
                    //销毁登陆活动时，携带用户的username返回
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                val rank = data!!.getStringExtra("rank")
                val level = data.getIntExtra("level",0)
                val backData = data.getStringExtra("login_back")
                mTvUsername.text = backData
                mTvLevel.text = "等级:$level"
                mTvRank.text = "排名:$rank"
            }
        }

    }


}