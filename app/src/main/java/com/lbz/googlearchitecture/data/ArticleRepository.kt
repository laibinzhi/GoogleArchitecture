package com.lbz.googlearchitecture.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.model.Article
import kotlinx.coroutines.flow.Flow

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:18
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleRepository(private val service: ArticleService) {

    fun getArticlestStream(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { ArticlePagingSource(service) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }
}