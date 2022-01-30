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
    fun timeStampToTime(timeStamp:Long):String{
        val sdf=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val sd=sdf.format(timeStamp)
        return sd
    }
}
