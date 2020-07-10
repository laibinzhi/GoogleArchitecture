package com.lbz.googlearchitecture.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.data.ArticleRepository

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ViewModelFactory(private val repository: ArticleRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArticlesitoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArticlesitoriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}