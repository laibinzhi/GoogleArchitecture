package com.lbz.googlearchitecture.di

import com.lbz.googlearchitecture.api.ArticleService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author: laibinzhi
 * @date: 2020-07-12 17:17
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Module
@InstallIn(ActivityComponent::class)
object NetworkModule {

    @Provides
    fun provideArticleService(okHttpClient: OkHttpClient): ArticleService {
        return Retrofit.Builder()
            .baseUrl(ArticleService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ArticleService::class.java)
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    fun providehttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }


}