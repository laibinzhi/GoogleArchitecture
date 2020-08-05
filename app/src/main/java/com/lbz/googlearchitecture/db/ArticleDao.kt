package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.ArticleType
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

    @Query("SELECT * FROM articles WHERE articleType =:articleType")
    fun getLocalArticles(articleType: Int): PagingSource<Int, Article>

    @Query("SELECT COUNT(*) FROM articles WHERE articleType=:articleType")
    fun getLocalArticleSize(articleType: Int): Int

    @Query("DELETE FROM articles WHERE articleType =:articleType")
    suspend fun clearArticlesByType(articleType: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banners: List<Banner>)

    @Query("SELECT * FROM banner ")
    fun getLocalBanner(): LiveData<List<Banner>>

    //获取本地titles数据，用户判断和网络数据是否相等
    @Query("SELECT * FROM banner ")
    fun getLocalBannerNotLiveData(): List<Banner>

    @Query("DELETE FROM banner")
    suspend fun clearBanner()

    @Query("UPDATE articles SET collect =:collect WHERE id =:id")
    suspend fun updateArticleCollect(id: Int, collect: Boolean)

    @Query("UPDATE articles SET collect =:collect")
    suspend fun updateAllArticleUnCollect(collect: Boolean)
}