package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lbz.googlearchitecture.model.NavigationResponse
import com.lbz.googlearchitecture.model.TreeSystem

/**
 * @author: laibinzhi
 * @date: 2020-08-05 09:24
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface SquareDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTreeSystemAll(treeSystem: List<TreeSystem>)

    @Query("SELECT * FROM tree_system ")
    fun getLocalTreeSystems(): LiveData<List<TreeSystem>>

    @Query("SELECT * FROM tree_system ")
    fun getLocalTreeSystemNotLiveData(): List<TreeSystem>

    @Query("DELETE FROM tree_system")
    suspend fun clearTreeSystems()

    @Query("SELECT * FROM navigation_response")
    fun getNavigations(): LiveData<List<NavigationResponse>>

    @Query("SELECT * FROM navigation_response")
    fun getNavigationsNotLiveData(): List<NavigationResponse>

    @Query("DELETE FROM navigation_response")
    suspend fun clearNavigation()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNavigation(navigation: List<NavigationResponse>)


}