package com.lbz.googlearchitecture.data.project

import androidx.paging.PagingSource
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.model.ProjectData
import retrofit2.HttpException
import java.io.IOException

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:14
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val ARTICLE_STARTING_PAGE_INDEX = 1

class ArticlePagingSource(private val service: LbzService, val cid: Int) :
    PagingSource<Int, ProjectData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProjectData> {
        val position = params.key ?: ARTICLE_STARTING_PAGE_INDEX
        return try {
            val response = service.getProjectArticles(position, cid)
            val repos = response.data.datas
            val hasNextPage = if (response.data.total <= response.data.size) {
                false
            }else{
                response.data.curPage != response.data.pageCount
            }

            LoadResult.Page(
                data = repos,
                prevKey = null,
                nextKey = if (hasNextPage) position + 1 else  null
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
