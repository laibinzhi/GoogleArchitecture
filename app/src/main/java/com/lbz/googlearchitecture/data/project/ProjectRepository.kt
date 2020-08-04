package com.lbz.googlearchitecture.data.project

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.model.ProjectData
import com.lbz.googlearchitecture.model.ProjectTitle
import com.lbz.googlearchitecture.utils.CollectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-15 16:31
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val TAG = "ProjectRepository"

enum class ProjectStatus { LOADING, ERROR, DONE }

class ProjectRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    val projectTitles: LiveData<List<ProjectTitle>> = database.projectDao().getLocalProjectTitle()

    //顶部项目题目的获取状态
    private val _status = MutableLiveData<ProjectStatus>()
    val status: LiveData<ProjectStatus>
        get() = _status

    suspend fun getProjectTitles() {
        _status.postValue(ProjectStatus.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val titlesFromNet = service.getProjectTitleAsync().await()
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val titlesFromDb = database.projectDao().getLocalProjectTitleNotLiveData()
                if (!CollectionUtil.compareTwoList(titlesFromDb, titlesFromNet.data)) {
                    Log.d(TAG, "从网上获取的题目列表和从数据库获取的题目列表不一致，须重新插入")
                    database.projectDao().clearProjectTitle()
                    database.projectDao().insertProjectTitleAll(titlesFromNet.data)
                }
                _status.postValue(ProjectStatus.DONE)
            } catch (e: Exception) {
                Log.e(TAG, "ProjectRepository获取getProjectTitles报错：" + e.message+"   size"+database.projectDao().getLocalProjectTitleNotLiveData().size)
                if (database.projectDao().getLocalProjectTitleNotLiveData().isNotEmpty()){
                    _status.postValue(ProjectStatus.DONE)
                }else{
                    _status.postValue(ProjectStatus.ERROR)
                }
                e.printStackTrace()
            }

        }

    }

    fun getProjectDataStream(cid: Int): Flow<PagingData<ProjectData>> {
        val pagingSourceFactory = { database.projectDao().getLocalProjectDataByCid(cid) }

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE * 2
            ),
            remoteMediator = ProjectRemoteMediator(
                cid,
                service,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun updateProjectCollectStatus(id: Int, collect: Boolean) {
        withContext(Dispatchers.IO) {
            try {
                database.projectDao().updateProjectCollect(id, collect)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    suspend fun updateAllProjectUnCollect() {
        withContext(Dispatchers.IO) {
            try {
                database.projectDao().updateAllProjectUnCollect(false)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 15
    }
}