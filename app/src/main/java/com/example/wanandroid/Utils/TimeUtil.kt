package com.example.wanandroid.Utils

import android.annotation.SuppressLint
import android.os.Build
import android.provider.ContactsContract
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
object TimeUtil {


    fun timestampToTime(timestamp:Long):String{
       return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
           SimpleDateFormat("YY-MM-DD-hh-mm-ss").format(timestamp)
       } else {
           TODO("VERSION.SDK_INT < N")
       }

    }
}