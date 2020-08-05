package com.lbz.googlearchitecture.data.square

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.data.article.ArticleRemoteMediator
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.model.Article
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-08-05 09:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class SquareRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    private var _articleType: Int = 0

    private val pagingSourceFactory = { database.articleDao().getLocalArticles(_articleType) }

    fun getArticles(articleType: Int): Flow<PagingData<Article>> {
        _articleType = articleType
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = 2 * NETWORK_PAGE_SIZE
            ),
            remoteMediator = ArticleRemoteMediator(
                service,
                database,
                articleType
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }

}