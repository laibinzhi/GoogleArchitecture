package com.lbz.googlearchitecture.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.data.search.SearchRepository
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class SearchViewModelFactory @Inject constructor(private val repository: SearchRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}