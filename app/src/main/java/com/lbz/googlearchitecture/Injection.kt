package com.lbz.googlearchitecture

import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.data.ArticleRepository
import com.lbz.googlearchitecture.ui.ViewModelFactory

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:37
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
object Injection {

    private fun provideGithubRepository(): ArticleRepository {
        return ArticleRepository(ArticleService.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideGithubRepository())
    }
}