package com.lbz.googlearchitecture.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.db.ArticleDatabase
import com.lbz.googlearchitecture.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:18
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleRepository @Inject constructor(
    private val service: ArticleService,
    private val database: ArticleDatabase
) {

    private val pagingSourceFactory = { database.articleDao().getLocalArticles() }

    fun getArticlestStream(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE
            ),
            remoteMediator = ArticleRemoteMediator(service, database),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }
}