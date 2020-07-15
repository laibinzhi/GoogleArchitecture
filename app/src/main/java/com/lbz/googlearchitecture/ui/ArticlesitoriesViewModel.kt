package com.lbz.googlearchitecture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lbz.googlearchitecture.data.ArticleRepository
import com.lbz.googlearchitecture.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:34
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
class ArticlesitoriesViewModel @Inject constructor(private val repository: ArticleRepository) :
    ViewModel() {

    fun getArticles(): Flow<PagingData<Article>> = repository.getArticlestStream()
        .cachedIn(viewModelScope)

}