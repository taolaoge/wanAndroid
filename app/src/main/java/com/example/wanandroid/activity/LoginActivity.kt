package com.example.wanandroid.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.wanandroid.bean.LoginResponse
import com.example.wanandroid.bean.MyselfResponse
import com.example.wanandroid.R
import com.example.wanandroid.utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException

/**
 * description ： 登录
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
class LoginActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var intent1: Intent
    lateinit var mEdUsername: EditText
    lateinit var mEdPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val toolbar: Toolbar = findViewById(R.id.login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        initView()
    }

    private fun initView() {
        intent1 = Intent(this,MainActivity::class.java)
        val mTvGoRegister: TextView = findViewById(R.id.login_tv_go_register)
        mTvGoRegister.setOnClickListener(this)
        val mBtnLogin: Button = findViewById(R.id.login_btn_login)
        mBtnLogin.setOnClickListener(this)
        mEdUsername = findViewById(R.id.login_ed_username)
        mEdPassword = findViewById(R.id.login_ed_password)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_tv_go_register -> {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.login_btn_login -> login()
        }
    }


    private fun login() {
        val username = mEdUsername.text.toString()
        val password = mEdPassword.text.toString()
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        HttpUtil.sendOkHttpPostRequest(
            "https://www.wanandroid.com/user/login",
            requestBody,"",
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val strBuilder = StringBuilder()
                    response.headers.forEach {
                        if (it.first == "Set-Cookie") {
                            strBuilder.append(it.second).append(";")
                        }
                    }
                    val gson = Gson()
                    val loginResponse = gson.fromJson(responseData, LoginResponse::class.java)
                    val message = loginResponse.errorMsg
                    if (message == "") {
                        //设置用户的个人信息,拼接完毕cookie并转为String类型
                        setMyselfMessage(strBuilder.toString(), loginResponse.data.username)
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                loginResponse.errorMsg,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            })
    }

    private fun setMyselfMessage(header: String, username: String) {
        //携带cookie请求头发起网络请求
        HttpUtil.sendOkHttpGetRequest(
            "https://wanandroid.com//user/lg/userinfo/json",
            header,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson = Gson()
                    val loginResponse = gson.fromJson(responseData, MyselfResponse::class.java)
                    val level = loginResponse.data.coinInfo.level
                    val rank = loginResponse.data.coinInfo.rank
                    //为什么不在一个线程内，intent1无法储存信息呢
                    intent1.putExtra("level", level)
                    intent1.putExtra("rank", rank)
                    intent1.putExtra("login_back", username)
                    setSharedPreference(header,username)
                    //回调onActivityResult,首次登录通知主页面更新ui
                    finish()
                    startActivity(intent1)
                }
            })
    }

    private fun setSharedPreference(cookie: String,username: String) {
        //获取sp的editor对象
        val editor = getSharedPreferences("cookie", Context.MODE_PRIVATE).edit()
        editor.putString("cookie", cookie)
        editor.putString("username",username)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //点击toolbar的返回按钮 finish此活动
        when (item.itemId) {
            android.R.id.home -> {
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val intent=Intent(this,MainActivity::class.java)
        startActivity(intent)
        return super.onKeyDown(keyCode, event)
    }
}