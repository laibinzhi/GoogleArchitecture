package com.lbz.googlearchitecture.api

import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Banner
import com.lbz.googlearchitecture.model.ProjectData
import com.lbz.googlearchitecture.model.ProjectTitle
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author: laibinzhi
 * @date: 2020-07-10 22:52
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
interface LbzService {

    @GET("/article/list/{page}/json")
    suspend fun getArticles(@Path("page") page: Int): DataResponse<PageBean<Article>>

    @GET("/project/list/{page}/json")
    suspend fun getProjectArticles(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): DataResponse<PageBean<ProjectData>>

    @GET("/project/tree/json")
    fun getProjectTitleAsync(): Deferred<DataResponse<List<ProjectTitle>>>

    @GET("/banner/json")
    fun getBannerAsync(): Deferred<DataResponse<List<Banner>>>

    @GET("/article/top/json")
    fun getTopArticlesAsync(): Deferred<DataResponse<List<Article>>>

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }
}