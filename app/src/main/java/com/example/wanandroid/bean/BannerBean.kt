package com.example.wanandroid.bean

import android.media.MediaDrm

/**
 * description ： TODO:类的作用
 * author :Li Jian
 * email : 1678921845@qq.com
 * date : 2022/2/5
 */
data class BannerBean(
    var data: List<Data>,
    var errorCode:Int,
    var errorMsg:String,
){
    data class Data(
        var imagePath:String,
        var title:String,
        var uri:String,
    )
}
