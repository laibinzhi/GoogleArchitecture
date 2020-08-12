package com.lbz.googlearchitecture.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lbz.googlearchitecture.model.*

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:31
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Database(
    entities = [Article::class, RemoteKeys::class, ProjectTitle::class, ProjectData::class, ProjectRemoteKeys::class, Banner::class, Hotkey::class, SearchHistory::class, User::class, TreeSystem::class, NavigationResponse::class],
    version = 1,
    exportSchema = false
)
abstract class LbzDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectRemoteKeysDao(): ProjectRemoteKeysDao
    abstract fun searchDao(): SearchDao
    abstract fun userDao(): UserDao
    abstract fun squareDao(): SquareDao

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
            )
//                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration().build()

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE `search_history` (`history` TEXT, " +
                            "PRIMARY KEY(`history`))"
                )
            }
        }
    }

}

