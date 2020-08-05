package com.lbz.googlearchitecture.data.article

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lbz.googlearchitecture.api.DataResponse
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.api.PageBean
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.db.RemoteKeys
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.ArticleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val ARTICLE_STARTING_PAGE_INDEX = 0

private const val TAG = "ArticleRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val service: LbzService,
    private val database: LbzDatabase,
    private val articleType: Int
) : RemoteMediator<Int, Article>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {

        var page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: ARTICLE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    throw InvalidObjectException("Remote key should not be null for $loadType")
                }
                remoteKeys.nextKey
            }
        }

        var localArticleSize = 0 //本地数据库数据大小

        database.withTransaction {
            localArticleSize = database.articleDao().getLocalArticleSize(articleType)
        }

        try {

//            val apiResponse = service.getArticles(page)
//            val articles = apiResponse.data.datas

            val articles = getArticleFromNet(page).data.datas
            articles.forEach {
                it.articleType = articleType
            }
            val endOfPaginationReached = articles.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys(articleType)
                    database.articleDao().clearArticlesByType(articleType)
                }
                val prevKey = if (page == ARTICLE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(
                        articleId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey,
                        articleType = articleType
                    )
                }
                database.remoteKeysDao().insertAll(keys)
                database.articleDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return if (loadType == LoadType.REFRESH) {
                if (localArticleSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        } catch (exception: HttpException) {
            return if (loadType == LoadType.REFRESH) {
                if (localArticleSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                database.remoteKeysDao().remoteKeysArticleId(repo.id, articleType)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                database.remoteKeysDao().remoteKeysArticleId(repo.id, articleType)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysArticleId(repoId, articleType)
            }
        }
    }

    private suspend fun getArticleFromNet(page: Int): DataResponse<PageBean<Article>> {
        return withContext(Dispatchers.IO) {
            val data = async {
                when (articleType) {
                    ArticleType.HOME_ARTICLE -> {
                        service.getArticles(page)
                    }
                    ArticleType.PLAZA_ARTICLE -> {
                        service.getSquareData(page)
                    }
                    ArticleType.ASK_ARTICLE -> {
                        service.getAskData(page)
                    }
                    else -> {
                        service.getArticles(page)
                    }
                }
            }
            Log.e(TAG, "获取正常列表size:" + data.await().data.datas.size)
            if (page == 0) {
                if (articleType == ArticleType.HOME_ARTICLE) {
                    val topData = async { service.getTopArticlesAsync() }
                    Log.e(TAG, "获取置顶列表size:" + topData.await().data.size)
                    data.await().data.datas.addAll(0, topData.await().data)
                }
                Log.e(TAG, "最后获取大小:" + data.await().data.datas.size)
                data.await()
            } else {
                Log.e(TAG, "最后获取大小:" + data.await().data.datas.size)
                data.await()
            }
        }
    }

}