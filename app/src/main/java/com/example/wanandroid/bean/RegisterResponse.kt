package com.example.wanandroid.bean

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/26
 */
data class RegisterResponse(
    var data:Data,
    var errorCode:Int,
    var errorMsg:String,
){
    data class Data(
        var id: Int,
        var username: String,
        var password: String,
        var icon: String?,
        var type: Int,
        var publicName:String
    )
}
