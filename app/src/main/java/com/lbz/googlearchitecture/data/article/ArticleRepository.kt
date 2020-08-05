package com.lbz.googlearchitecture.data.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.ArticleType
import com.lbz.googlearchitecture.model.Banner
import com.lbz.googlearchitecture.utils.CollectionUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:18
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val TAG = "ArticleRepository"

enum class Status { LOADING, ERROR, DONE }

class ArticleRepository @Inject constructor(
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

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val banner: LiveData<List<Banner>> = database.articleDao().getLocalBanner()

    suspend fun getBanner() {
        _status.postValue(Status.LOADING)
        withContext(Dispatchers.IO) {
            try {
                val bannerFromNet = service.getBannerAsync().await()
                //判断从网络上获取的和从数据库获取的是否相同，相同的就不返回了
                val bannerFromDb = database.articleDao().getLocalBannerNotLiveData()
                if (!CollectionUtil.compareTwoList(bannerFromDb, bannerFromNet.data)) {
                    Log.d(TAG, "从网上获取的banner列表和从数据库获取的题目列表不一致，须重新插入")
                    database.articleDao().clearBanner()
                    database.articleDao().insertBanner(bannerFromNet.data)
                }
                _status.postValue(Status.DONE)
            } catch (e: Exception) {
                Log.e(TAG, "ArticleRepository获取banner报错：" + e.message)
                _status.postValue(Status.ERROR)
                e.printStackTrace()
            }
        }
    }

    suspend fun updateArticleCollectStatus(id: Int, collect: Boolean) {
        withContext(Dispatchers.IO) {
            database.articleDao().updateArticleCollect(id, collect)
        }
    }

    suspend fun updateAllArticleUnCollect() {
        withContext(Dispatchers.IO) {
            database.articleDao().updateAllArticleUnCollect(false)
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 30
    }
}