package com.example.wanandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.wanandroid.Bean.LoginResponse
import com.example.wanandroid.Bean.RegisterResponse
import com.example.wanandroid.R
import com.example.wanandroid.Utils.HttpUtil
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.Response
import java.io.IOException

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mEdUsername: EditText
    lateinit var mEdPassword: EditText
    lateinit var mEdRePassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val toolbar: Toolbar = findViewById(R.id.register_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
        initView()
    }

    private fun initView() {
        val mTvGoLogin: TextView = findViewById(R.id.login_tv_go_login)
        mTvGoLogin.setOnClickListener(this)
        val mBtnRegister: Button = findViewById(R.id.register_btn_register)
        mBtnRegister.setOnClickListener(this)
        mEdUsername = findViewById(R.id.register_ed_username)
        mEdPassword = findViewById(R.id.register_ed_password)
        mEdRePassword = findViewById(R.id.register_ed_re_password)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_btn_register -> register()
            R.id.login_tv_go_login -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun register() {
        val username = mEdUsername.text.toString()
        val password = mEdPassword.text.toString()
        val rePassword = mEdRePassword.text.toString()
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .add("repassword", rePassword)
            .build()
        HttpUtil.sendOkHttpPostRequest(
            "https://www.wanandroid.com/user/register",
            requestBody,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val responseData = response.body?.string()
                    val gson=Gson()
                    val registerResponse=gson.fromJson(responseData,RegisterResponse::class.java)
                    val message=registerResponse.errorMsg
                    if (message!! == ""){
                        val intent=Intent(this@RegisterActivity,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        runOnUiThread{
                            Toast.makeText(this@RegisterActivity,registerResponse.errorMsg,Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}