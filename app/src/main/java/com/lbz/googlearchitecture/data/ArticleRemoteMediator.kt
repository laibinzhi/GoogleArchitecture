package com.lbz.googlearchitecture.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lbz.googlearchitecture.db.RemoteKeys
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.db.ArticleDatabase
import com.lbz.googlearchitecture.model.Article
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

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(
    private val service: ArticleService,
    private val database: ArticleDatabase
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
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                remoteKeys.prevKey
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
            localArticleSize = database.articleDao().getLocalArticleSize()
        }

        try {
            val apiResponse = service.getArticles(page)

            val articles = apiResponse.data.datas
            val endOfPaginationReached = articles.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().clearRemoteKeys()
                    database.articleDao().clearArticles()
                }
                val prevKey = if (page == ARTICLE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(articleId = it.id, prevKey = prevKey, nextKey = nextKey)
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
                database.remoteKeysDao().remoteKeysArticleId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                database.remoteKeysDao().remoteKeysArticleId(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Article>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                database.remoteKeysDao().remoteKeysArticleId(repoId)
            }
        }
    }

}