package com.example.wanandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.wanandroid.Fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var lastIndex = 0
    lateinit var fragment0: Fragment
    lateinit var fragment1: Fragment
    lateinit var fragment2: Fragment
    lateinit var fragment3: Fragment
    lateinit var fragment4: Fragment
    var fragments = arrayListOf<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        initData()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_first -> setFragmentPosition(0)
                R.id.bottom_square -> setFragmentPosition(1)
                R.id.bottom_wechat -> setFragmentPosition(2)
                R.id.bottom_system -> setFragmentPosition(3)
                R.id.bottom_item -> setFragmentPosition(4)
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
        fragments.add(fragment0)
        fragments.add(fragment1)
        fragments.add(fragment2)
        fragments.add(fragment3)
        fragments.add(fragment4)
        setFragmentPosition(0)
    }

    private fun setFragmentPosition(position: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val currentFragment=fragments[position]
        val lastFragment=fragments[lastIndex]
        lastIndex=position
        //隐藏上一个fragment
        transaction.hide(lastFragment)
        //这个if语句什么意思呢？
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
}