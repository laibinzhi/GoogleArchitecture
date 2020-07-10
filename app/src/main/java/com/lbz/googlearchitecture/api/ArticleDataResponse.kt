package com.lbz.googlearchitecture.api

import com.google.gson.annotations.SerializedName
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-07-10 22:57
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
data class ArticleDataResponse(
    @SerializedName("errorMsg") val errorMsg: String,
    @SerializedName("errorCode") val errorCode: Int,
    @SerializedName("data") val data: PageBean
)

data class PageBean(
    @SerializedName("curPage") val curPage: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("over") val over: Boolean,
    @SerializedName("pageCount") val pageCount: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("datas") val datas: List<Article> = emptyList()
)

