package com.example.wanandroid.bean

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/15
 */
data class GradeBean(
    val data:Data,
    val errorCode:Int,
    val errorMsg:String
) {
    data class Data(
        val coinCount: Int,
        val level: Int,
        val rank: String
    )
}
