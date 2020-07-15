package com.lbz.googlearchitecture.api

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author: laibinzhi
 * @date: 2020-07-10 22:52
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
interface ArticleService {

    @GET("/article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): ArticleDataResponse

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }
}