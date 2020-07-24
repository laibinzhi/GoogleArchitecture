package com.lbz.googlearchitecture.data.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Hotkey
import com.lbz.googlearchitecture.model.SearchHistory
import com.lbz.googlearchitecture.utils.CollectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-22 16:56
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
enum class Status { LOADING, ERROR, DONE }

private const val TAG = "SearchRepository"

private const val SHOW_SEARCH_HISTORY_SIZE = 10

class SearchRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    val hotkeys: LiveData<List<Hotkey>> = database.searchDao().getLocalHotKey()

    val searchHistory: LiveData<List<SearchHistory>> =
        database.searchDao().getLocalHistory(SHOW_SEARCH_HISTORY_SIZE)

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    /**
     * 获取热门关键词列表
     */
    suspend fun getHotKeys() {
        _status.postValue(Status.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val hotkeyFromNet = service.getHotKeyDataAsync().await()
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val hotkeyFromDb = database.searchDao().getLocalHotKeyNotLiveData()
                if (!CollectionUtil.compareTwoList(hotkeyFromDb, hotkeyFromNet.data)) {
                    Log.d(TAG, "从网上获取的hotkey列表和从数据库获取的hotkey列表不一致，须重新插入")
                    database.searchDao().clearHotkey()
                    database.searchDao().insertHotKey(hotkeyFromNet.data)
                }
                _status.postValue(Status.DONE)
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "SearchRepository获取getHotKeys报错：" + e.message + "   size" + database.searchDao()
                        .getLocalHotKeyNotLiveData().size
                )
                if (database.searchDao().getLocalHotKeyNotLiveData().isNotEmpty()) {
                    _status.postValue(Status.DONE)
                } else {
                    _status.postValue(Status.ERROR)
                }
                e.printStackTrace()
            }

        }

    }

    /**
     * 插入一个搜索历史
     */
    suspend fun insertHistory(query: String) {
        val history = SearchHistory(history = query)
        withContext(Dispatchers.IO) {
            try {
                database.searchDao().clearSearchHistory(query)
                database.searchDao().insertHistory(history)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 清除所有搜索历史
     */
    suspend fun clearAllHistory() {
        withContext(Dispatchers.IO) {
            database.searchDao().clearAllSearchHistory()
        }
    }

    /**
     * 清除某一个搜索历史
     */
    suspend fun clearSearchHistory(query: String) {
        withContext(Dispatchers.IO) {
            database.searchDao().clearSearchHistory(query)
        }
    }


    fun getSearchResult(search: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = NETWORK_PAGE_SIZE * 2
            ),
            pagingSourceFactory = { SearchPagingSource(service, search) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

}