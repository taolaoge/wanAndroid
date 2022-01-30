package com.example.wanandroid.bean

import java.io.Serializable

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/30
 */
data class MyselfResponse(
    val data: Data,
    val errorCode:Int,
    val errorMsg:String
){
    data class Data(
    val coinInfo:CoinInfo,
    val userInfo:CoinInfo.UserInfo
    ):Serializable{
        data class CoinInfo(
            val coinCount:Int,
            val level: Int,
            val nickname:String,
            val rank:String,
            val userId:Int,
            val username:String
        ):Serializable{
            data class UserInfo(
                val admin:Boolean,
                val coinCount:Int,
                val id:Int,
            )
        }
    }
}
