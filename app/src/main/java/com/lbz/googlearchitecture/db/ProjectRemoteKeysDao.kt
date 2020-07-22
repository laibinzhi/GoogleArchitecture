package com.lbz.googlearchitecture.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:31
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface ProjectRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ProjectRemoteKeys>)

    @Query("SELECT * FROM project_remote_keys WHERE projectId = :projectId")
    suspend fun remoteKeysProjectId(projectId: Int): ProjectRemoteKeys?

    @Query("DELETE FROM project_remote_keys WHERE cid =:cid")
    suspend fun clearProjectRemoteKeysbycid(cid: Int)
}

