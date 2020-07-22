package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Banner

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:29
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("SELECT * FROM articles ")
    fun getLocalArticles(): PagingSource<Int, Article>

    @Query("SELECT COUNT(*) FROM articles")
    fun getLocalArticleSize(): Int

    @Query("DELETE FROM articles")
    suspend fun clearArticles()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banners: List<Banner>)

    @Query("SELECT * FROM banner ")
    fun getLocalBanner(): LiveData<List<Banner>>

    //获取本地titles数据，用户判断和网络数据是否相等
    @Query("SELECT * FROM banner ")
    fun getLocalBannerNotLiveData(): List<Banner>

    @Query("DELETE FROM banner")
    suspend fun clearBanner()

}