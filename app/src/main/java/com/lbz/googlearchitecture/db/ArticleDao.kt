package com.lbz.googlearchitecture.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lbz.googlearchitecture.model.Article

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
}