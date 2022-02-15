package com.example.wanandroid.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.wanandroid.R
import com.example.wanandroid.bean.GradeBean
import com.example.wanandroid.bean.MyselfResponse
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttp
import okhttp3.Response
import java.io.IOException

class MyGradeActivity : AppCompatActivity() {
    lateinit var mTvGrade:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_grade)
        val toolbar: Toolbar = findViewById(R.id.grade_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        mTvGrade=findViewById(R.id.grade_grade)
        loadMoreData()
    }

    private fun loadMoreData() {
        val prefs = getSharedPreferences("cookie", Context.MODE_PRIVATE)
        val cookie = prefs?.getString("cookie", "") ?: ""
        HttpUtil.sendOkHttpGetRequest("https://www.wanandroid.com/lg/coin/userinfo/json",
            cookie,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
                    //网络请求成功后，解析json数据
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val gradeResponse = gson.fromJson(responseData, GradeBean::class.java)
                    val grade=gradeResponse.data.coinCount
                    runOnUiThread {
                        mTvGrade.text=grade.toString()
                    }
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //点击toolbar的返回按钮 finish此活动
        when (item.itemId) {
            android.R.id.home -> {
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}