package com.lbz.googlearchitecture.data.search

import androidx.paging.PagingSource
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.model.Article
import retrofit2.HttpException
import java.io.IOException

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:14
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val ARTICLE_STARTING_PAGE_INDEX = 0

private const val TAG = "SearchPagingSource"

class SearchPagingSource(private val service: LbzService, val search: String) :
    PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = service.getSearchDataByKey(position, search)
            val repos = response.data.datas
            if (response.data.total == 0) {
                throw EmptyDataException(SEARCH_RESULT_EMPTY)
            }

            val hasNextPage = if (response.data.total <= response.data.size) {
                false
            } else {
                response.data.curPage != response.data.pageCount
            }

            LoadResult.Page(
                data = repos,
                prevKey = null,
                nextKey = if (hasNextPage) position + 1 else null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: EmptyDataException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val SEARCH_RESULT_EMPTY = "search result is empty"
    }
}
