package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lbz.googlearchitecture.model.ProjectData
import com.lbz.googlearchitecture.model.ProjectTitle

/**
 * @author: laibinzhi
 * @date: 2020-07-15 16:24
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectTitleAll(articles: List<ProjectTitle>)

    @Query("SELECT * FROM project_titles ")
    fun getLocalProjectTitle(): LiveData<List<ProjectTitle>>

    //获取本地titles数据，用户判断和网络数据是否相等
    @Query("SELECT * FROM project_titles ")
    fun getLocalProjectTitleNotLiveData(): List<ProjectTitle>

    @Query("DELETE FROM project_titles")
    suspend fun clearProjectTitle()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ProjectData>)

    @Query("SELECT * FROM project_data WHERE chapterId = :cid")
    fun getLocalProjectDataByCid(cid: Int): PagingSource<Int, ProjectData>

    @Query("SELECT COUNT(*) FROM project_data WHERE chapterId = :cid")
    fun getLocalProjectDataSizeByCid(cid: Int): Int

    @Query("DELETE FROM project_data WHERE chapterId = :cid")
    suspend fun clearLocalProjectDataByCid(cid: Int)

    @Query("UPDATE project_data SET collect =:collect WHERE id =:id")
    suspend fun updateProjectCollect(id: Int, collect: Boolean)

    @Query("UPDATE project_data SET collect =:collect")
    suspend fun updateAllProjectUnCollect(collect: Boolean)

}