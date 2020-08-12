package com.lbz.googlearchitecture.ui.square

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lbz.googlearchitecture.data.square.SquareRepository
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.ArticleType
import com.lbz.googlearchitecture.model.NavigationResponse
import com.lbz.googlearchitecture.model.TreeSystem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * @author: laibinzhi
 * @date: 2020-08-05 09:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
class SquareViewModel @ViewModelInject constructor(private val repository: SquareRepository) :
    ViewModel() {

    //获取广场数据
    fun getPlazaData(): Flow<PagingData<Article>> =
        repository.getArticles(ArticleType.PLAZA_ARTICLE)
            .cachedIn(viewModelScope)

    //每日一问
    fun getAskData(): Flow<PagingData<Article>> = repository.getArticles(ArticleType.ASK_ARTICLE)
        .cachedIn(viewModelScope)

    val status = repository.status

    val treeSystems: LiveData<List<TreeSystem>> = repository.treeSystems
    val navigations: LiveData<List<NavigationResponse>> = repository.navigations

    fun getTreeSystems() {
        viewModelScope.launch {
            repository.getTreeSystems()
        }
    }

    fun getNavigations() {
        viewModelScope.launch {
            repository.getNavigations()
        }

    }

}