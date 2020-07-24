package com.lbz.googlearchitecture.api

import com.google.gson.annotations.SerializedName

/**
 * @author: laibinzhi
 * @date: 2020-07-10 22:57
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
data class DataResponse<T>(
    @SerializedName("errorMsg") val errorMsg: String,
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("data") val data: T
)

data class PageBean<T>(
    @SerializedName("curPage") val curPage: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("over") val over: Boolean,
    @SerializedName("pageCount") val pageCount: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("datas") val datas: MutableList<T>
)
