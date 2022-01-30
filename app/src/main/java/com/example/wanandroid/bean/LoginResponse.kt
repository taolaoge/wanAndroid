package com.example.wanandroid.bean

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/26
 */
data class LoginResponse(
    var errorCode: Int,
    var errorMsg: String?,
    var data: Data
) {
    data class Data(
        var id: Int,
        var username: String,
        var password: String,
        var icon: String?,
        var type: Int,
        var collectIds: List<Int>?,
    )
}
