package com.example.wanandroid.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.wanandroid.R

class LoginActivity : AppCompatActivity() ,View.OnClickListener{
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
        val mTvGoRegister:TextView=findViewById(R.id.login_tv_go_register)
        mTvGoRegister.setOnClickListener(this)
        val mBtnLogin:Button=findViewById(R.id.login_btn_login)
        mBtnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.login_tv_go_register->{
                val intent=Intent(this,RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.login_btn_login->login()
        }
    }

    private fun login() {

    }
}