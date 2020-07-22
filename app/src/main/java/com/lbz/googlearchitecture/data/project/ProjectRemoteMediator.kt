package com.lbz.googlearchitecture.data.project

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.db.ProjectRemoteKeys
import com.lbz.googlearchitecture.model.ProjectData
import retrofit2.HttpException
import java.io.IOException

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val ARTICLE_STARTING_PAGE_INDEX = 1
private const val TAG = "ProjectRemoteMediator"

@OptIn(ExperimentalPagingApi::class)
class ProjectRemoteMediator(
    private val cid: Int,
    private val service: LbzService,
    private val database: LbzDatabase
) : RemoteMediator<Int, ProjectData>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProjectData>
    ): MediatorResult {


        var page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: ARTICLE_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
//                val prevKey = remoteKeys.prevKey ?: return MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//                remoteKeys.prevKey
                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                if (remoteKeys?.nextKey == null) {
                    Log.d(TAG, "Remote key should not be null for $loadType")
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                remoteKeys.nextKey
            }
        }

        Log.e(TAG, "page:$page   loadType:$loadType")

        var localProjectDataByCideSize = 0 //本地数据库数据大小

        database.withTransaction {
            localProjectDataByCideSize = database.projectDao().getLocalProjectDataSizeByCid(cid)
        }

        try {
            val apiResponse = service.getProjectArticles(page, cid)

            val projectDatasfromNet = apiResponse.data.datas
            val hasNextPage = if (apiResponse.data.total <= apiResponse.data.size) {
                false
            } else {
                apiResponse.data.curPage != apiResponse.data.pageCount
            }

            val endOfPaginationReached = projectDatasfromNet.isEmpty() || !hasNextPage
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.projectRemoteKeysDao().clearProjectRemoteKeysbycid(cid)
                    database.projectDao().clearLocalProjectDataByCid(cid)
                }

                val prevKey = if (page == ARTICLE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = projectDatasfromNet.map {
                    ProjectRemoteKeys(
                        projectId = it.id,
                        cid = cid,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                database.projectRemoteKeysDao().insertAll(keys)
                database.projectDao().insertAll(projectDatasfromNet)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return if (loadType == LoadType.REFRESH) {
                if (localProjectDataByCideSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        } catch (exception: HttpException) {
            return if (loadType == LoadType.REFRESH) {
                if (localProjectDataByCideSize > 0) {
                    MediatorResult.Success(true)
                } else {
                    MediatorResult.Error(exception)
                }
            } else {
                MediatorResult.Error(exception)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProjectData>): ProjectRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { project ->
                database.projectRemoteKeysDao().remoteKeysProjectId(project.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProjectData>): ProjectRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { project ->
                database.projectRemoteKeysDao().remoteKeysProjectId(project.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ProjectData>
    ): ProjectRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { projectId ->
                database.projectRemoteKeysDao().remoteKeysProjectId(projectId)
            }
        }
    }

}