package com.example.wanandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wanandroid.Fragments.*
import com.example.wanandroid.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var lastIndex = 0
    lateinit var mTvUsername:TextView
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
        initView()
        initData()
        initBottomNavigation()
        initNavigation()
    }


    private fun initView() {
        val navView: NavigationView = findViewById(R.id.navView)
        val headerLayout = navView.inflateHeaderView(R.layout.nav_header)
        mTvUsername= headerLayout.findViewById(R.id.nav_tv_go)
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
                //判断是否登录成功
                if(mTvUsername.text=="去登陆") {
                    val intent = Intent(this, LoginActivity::class.java)
                    //销毁登陆活动时，携带用户的username返回
                    startActivityForResult(intent, 1)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->{
                val backData=data?.getStringExtra("login_back")?:"去登陆"
                mTvUsername.text=backData
            }
        }

    }
}