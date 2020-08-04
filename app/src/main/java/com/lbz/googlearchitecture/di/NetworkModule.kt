package com.lbz.googlearchitecture.di

import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lbz.googlearchitecture.api.LbzService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideArticleService(okHttpClient: OkHttpClient): LbzService {
        return Retrofit.Builder()
            .baseUrl(LbzService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(LbzService::class.java)
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,@ApplicationContext context: Context) = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor).cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context)))
        .build()

    @Provides
    fun providehttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        return httpLoggingInterceptor
    }


}