package com.lbz.googlearchitecture.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lbz.googlearchitecture.data.search.SearchRepository
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Hotkey
import com.lbz.googlearchitecture.model.SearchHistory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

/**
 * @author: laibinzhi
 * @date: 2020-07-22 16:55
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(private val repository: SearchRepository) :
    ViewModel() {

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getHotKeys() {
        viewModelScope.launch {
            repository.getHotKeys()
        }
    }

    fun insertHistory(query: String) {
        viewModelScope.launch {
            repository.insertHistory(query)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAllHistory()
        }
    }

    fun clearSearchHistory(query: String) {
        viewModelScope.launch {
            repository.clearSearchHistory(query)
        }
    }

    val status = repository.status

    val hotkeys: LiveData<List<Hotkey>> = repository.hotkeys

    val searchHistory: LiveData<List<SearchHistory>> = repository.searchHistory

    fun getSearchResult(search: String): Flow<PagingData<Article>> =
        repository.getSearchResult(search)
            .cachedIn(viewModelScope)
}