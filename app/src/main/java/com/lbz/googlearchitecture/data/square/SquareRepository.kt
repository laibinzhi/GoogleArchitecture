package com.lbz.googlearchitecture.data.square

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.data.article.ArticleRemoteMediator
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.NavigationResponse
import com.lbz.googlearchitecture.model.TreeSystem
import com.lbz.googlearchitecture.utils.CollectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-08-05 09:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
enum class Status { LOADING, ERROR, DONE }

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

    val treeSystems: LiveData<List<TreeSystem>> = database.squareDao().getLocalTreeSystems()
    val navigations: LiveData<List<NavigationResponse>> = database.squareDao().getNavigations()

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    suspend fun getTreeSystems() {
        _status.postValue(Status.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val treeSystemFromNet = service.getTreeSystemAsync().await()
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val  treeSystemFromDb = database.squareDao().getLocalTreeSystemNotLiveData()
                if (!CollectionUtil.compareTwoList(treeSystemFromDb, treeSystemFromNet.data)) {
                    database.squareDao().clearTreeSystems()
                    database.squareDao().insertTreeSystemAll(treeSystemFromNet.data)
                }
                _status.postValue(Status.DONE)
            } catch (e: Exception) {
                if (database.squareDao().getLocalTreeSystemNotLiveData().isNotEmpty()){
                    _status.postValue(Status.DONE)
                }else{
                    _status.postValue(Status.ERROR)
                }
                e.printStackTrace()
            }
        }
    }

    suspend fun getNavigations() {
        _status.postValue(Status.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val navigationFromNet = service.getNavigationDataAsync().await()
                val navigationDataFromNet = navigationFromNet.data
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val  navigationFromDb = database.squareDao().getNavigationsNotLiveData()
                if (!CollectionUtil.compareTwoList(navigationFromDb, navigationDataFromNet)) {
                    database.squareDao().clearNavigation()
                    database.squareDao().insertNavigation(navigationDataFromNet)
                }
                _status.postValue(Status.DONE)
            } catch (e: Exception) {
                if (database.squareDao().getNavigationsNotLiveData().isNotEmpty()){
                    _status.postValue(Status.DONE)
                }else{
                    _status.postValue(Status.ERROR)
                }
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }

}