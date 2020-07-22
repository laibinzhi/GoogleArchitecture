package com.lbz.googlearchitecture.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Banner
import com.lbz.googlearchitecture.model.ProjectData
import com.lbz.googlearchitecture.model.ProjectTitle

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:31
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Database(
    entities = [Article::class, RemoteKeys::class, ProjectTitle::class, ProjectData::class, ProjectRemoteKeys::class, Banner::class],
    version = 1,
    exportSchema = false
)
abstract class LbzDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectRemoteKeysDao(): ProjectRemoteKeysDao


    companion object {

        @Volatile
        private var INSTANCE: LbzDatabase? = null

        fun getInstance(context: Context): LbzDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                LbzDatabase::class.java, "lbz.db"
            ).build()
    }
}

