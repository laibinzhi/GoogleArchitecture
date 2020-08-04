package com.lbz.googlearchitecture.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lbz.googlearchitecture.data.article.ArticleRepository
import com.lbz.googlearchitecture.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:34
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel @ViewModelInject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    fun getArticles(): Flow<PagingData<Article>> = repository.getArticles()
        .cachedIn(viewModelScope)

    fun getBanner() {
        viewModelScope.launch {
            repository.getBanner()
        }
    }

    fun updateArticleCollectStatus(id:Int,collect:Boolean){
        viewModelScope.launch {
            repository.updateArticleCollectStatus(id,collect)
        }
    }

    fun updateAllArticleUnCollect(){
        viewModelScope.launch {
            repository.updateAllArticleUnCollect()
        }
    }

    val status = repository.status

    val banner = repository.banner


}