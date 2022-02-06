package com.example.wanandroid.bean

import android.icu.text.CaseMap
import java.io.Serializable

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/7
 */
data class HomeArticleResponse(
    var errorCode: Int,
    var errorMsg: String?,
    var data: Data
){
    data class Data(
        var offset: Int,
        var size: Int,
        var total: Int,
        var pageCount: Int,
        var curPage: Int,
        var over: Boolean,
        var datas: List<Datas>?
    ):Serializable{
        data class Datas(
            var author:String,
            var shareUser:String,
            var link:String,
            var superChapterName:String,
            var shareData:Long,
            var title:String,
            var publishData:Long,
            var chapterName:String,
            var niceDate:String
        )
    }
}