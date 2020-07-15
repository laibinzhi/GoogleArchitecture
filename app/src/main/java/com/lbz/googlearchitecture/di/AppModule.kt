package com.lbz.googlearchitecture.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.api.ArticleService
import com.lbz.googlearchitecture.data.ArticleRepository
import com.lbz.googlearchitecture.db.ArticleDatabase
import com.lbz.googlearchitecture.ui.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * @author: laibinzhi
 * @date: 2020-07-12 17:21
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

@Module
@InstallIn(ActivityComponent::class)
object AppModule {

    @Provides
    fun provideArticleRepository(
        service: ArticleService,
        database: ArticleDatabase
    ): ArticleRepository {
        return ArticleRepository(service, database)
    }

    @Provides
    fun provideArticleDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return ArticleDatabase.getInstance(context)
    }


    @Provides
    fun provideViewModelFactory(articleRepository: ArticleRepository): ViewModelProvider.Factory {
        return ViewModelFactory(articleRepository)
    }


}