package com.lbz.googlearchitecture.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.data.article.ArticleRepository
import com.lbz.googlearchitecture.data.project.ProjectRepository
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.ui.home.ArticleViewModelFactory
import com.lbz.googlearchitecture.ui.project.ProjectViewModelFactory
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
        service: LbzService,
        database: LbzDatabase
    ): ArticleRepository {
        return ArticleRepository(
            service,
            database
        )
    }

    @Provides
    fun provideProjectRepository(
        service: LbzService,
        database: LbzDatabase
    ): ProjectRepository {
        return ProjectRepository(
            service,
            database
        )
    }

    @Provides
    fun provideLbzDatabase(@ApplicationContext context: Context): LbzDatabase {
        return LbzDatabase.getInstance(context)
    }


    @Provides
    fun provideArticleViewModelFactory(articleRepository: ArticleRepository): ViewModelProvider.Factory {
        return ArticleViewModelFactory(
            articleRepository
        )
    }

    @Provides
    fun provideProjectViewModelFactory(projectRepository: ProjectRepository): ViewModelProvider.Factory {
        return ProjectViewModelFactory(
            projectRepository
        )
    }

}