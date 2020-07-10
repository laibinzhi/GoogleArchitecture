package com.lbz.googlearchitecture.data

import androidx.paging.PagingSource
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.model.Article
import retrofit2.HttpException
import java.io.IOException

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:14
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val ARTICLE_STARTING_PAGE_INDEX = 1

class ArticlePagingSource(private val service: ArticleService) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = service.getArticles(position)
            val repos = response.data.datas
            LoadResult.Page(
                data = repos,
                prevKey = if (position == ARTICLE_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
