package com.lbz.googlearchitecture.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

        private const val BASE_URL = "https://www.wanandroid.com"

        fun create(): ArticleService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ArticleService::class.java)
        }
    }
}