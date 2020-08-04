package com.lbz.googlearchitecture.api

import com.lbz.googlearchitecture.model.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

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
    suspend fun getTopArticlesAsync(): DataResponse<List<Article>>

    @GET("/hotkey/json")
    fun getHotKeyDataAsync(): Deferred<DataResponse<List<Hotkey>>>

    @POST("/article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): DataResponse<PageBean<Article>>

    @FormUrlEncoded
    @POST("/user/login")
    fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): Deferred<DataResponse<User>>

    @GET("/user/logout/json")
    fun logout(): Deferred<DataResponse<User>>

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }
}