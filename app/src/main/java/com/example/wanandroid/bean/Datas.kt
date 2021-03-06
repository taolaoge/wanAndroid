package com.example.wanandroid.bean

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/1/24
 */
data class Datas(
    var id: Int,
    var originId: Int,
    var title: String,
    var chapterId: Int,
    var chapterName: String?,
    var envelopePic: String,
    var link: String,
    var author: String,
    var origin: Any,
    var publishTime: Long,
    var zan: Any,
    var desc: String,
    var shareUser:String,
    var visible: Int,
    var niceDate: String,
    var courseId: Int,
    var collect: Boolean,
)
