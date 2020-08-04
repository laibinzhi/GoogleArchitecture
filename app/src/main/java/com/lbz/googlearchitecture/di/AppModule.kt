package com.lbz.googlearchitecture.di

import android.content.Context
import com.lbz.googlearchitecture.db.LbzDatabase
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
    fun provideLbzDatabase(@ApplicationContext context: Context): LbzDatabase {
        return LbzDatabase.getInstance(context)
    }

}