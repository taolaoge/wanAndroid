package com.example.wanandroid.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wanandroid.bean.LoginResponse
import com.example.wanandroid.fragments.*
import com.example.wanandroid.R
import com.example.wanandroid.bean.MyselfResponse
import com.example.wanandroid.utils.HttpUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException
import java.net.HttpCookie
import android.widget.Toast




class MainActivity : AppCompatActivity(), View.OnClickListener {
    var lastIndex = 0
    var username=""
    private var mExitTime = 0L
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
        //cookie实现自动登录
        initLogin()
        initView()
        initData()
        initBottomNavigation()
        initNavigation()
        //为DrawerLayout中menu设置点击事件
        initNavClick()
    }

    private fun initLogin() {
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie: String = prefs.getString("cookie", "") ?: ""
        HttpUtil.sendOkHttpGetRequest(
            "https://wanandroid.com//user/lg/userinfo/json",
            cookie,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val loginResponse = gson.fromJson(responseData, MyselfResponse::class.java)
                    val errorMsg=loginResponse.errorMsg
                    if (errorMsg==""){
                        val level = loginResponse.data.coinInfo.level
                        val rank = loginResponse.data.coinInfo.rank
                        runOnUiThread {
                            val prefs=getSharedPreferences("cookie",Context.MODE_PRIVATE)
                            val username=prefs.getString("username","")
                            mTvUsername.text = username
                            mTvLevel.text = "等级:$level"
                            mTvRank.text = "排名:$rank"
                        }
                    }else{
                        runOnUiThread {
                            mTvUsername.text ="去登录"
                            mTvLevel.text = "等级:"
                            mTvRank.text = "排名:"
                        }
                    }
                }

            })
    }

    private fun initNavClick() {
        navView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.navGrade -> {
                    if(mTvUsername.text=="去登录"){
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, MyGradeActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navCollect -> {
                    if(mTvUsername.text=="去登录"){
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, MyStarActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navShare -> {
                    if(mTvUsername.text=="去登录"){
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else{
                        val intent = Intent(this, MyShareActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.navGoOut->{
                    if(mTvUsername.text=="去登录"){
                        Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show()
                        val intent=Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                    }else {
                        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE).edit()
                        prefs.clear()
                        prefs.apply()
                        //回调,重新设置用户数据
                        initLogin()
                        val drawerLayout: DrawerLayout = findViewById(R.id.drawerlayout)
                        drawerLayout.closeDrawers()
                    }
                    true
                }

                else -> {
                    true
                }
            }
        })
    }


    private fun initView() {
        navView = findViewById(R.id.navView)
        headerLayout = navView.inflateHeaderView(R.layout.nav_header)
        mTvUsername = headerLayout.findViewById(R.id.nav_tv_go)
        mTvLevel = headerLayout.findViewById(R.id.nav_lever)
        mTvRank = headerLayout.findViewById(R.id.nav_rank)
        mTvUsername.setOnClickListener(this)
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
                if (mTvUsername.text == "去登录") {
                    val intent = Intent(this, LoginActivity::class.java)
                    //销毁登陆活动时，携带用户的username返回
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                val rank = data?.getStringExtra("rank")?:""
                val level = data?.getIntExtra("level", 0)?:""
                username = data?.getStringExtra("login_back")?:"去登录"
                mTvUsername.text = username
                mTvLevel.text = "等级:$level"
                mTvRank.text = "排名:$rank"
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*
            * 当当前时间大于上次按返回键的时间 2 秒时
            */
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                mExitTime = System.currentTimeMillis()
                return false
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}

