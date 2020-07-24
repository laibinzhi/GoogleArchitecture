package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lbz.googlearchitecture.model.Hotkey
import com.lbz.googlearchitecture.model.SearchHistory

/**
 * @author: laibinzhi
 * @date: 2020-07-22 16:57
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotKey(hotkeys: List<Hotkey>)

    @Query("SELECT * FROM hotkey ")
    fun getLocalHotKey(): LiveData<List<Hotkey>>

    //获取本地hotkey数据，用户判断和网络数据是否相等
    @Query("SELECT * FROM hotkey ")
    fun getLocalHotKeyNotLiveData(): List<Hotkey>

    @Query("DELETE FROM hotkey")
    suspend fun clearHotkey()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: SearchHistory)

    @Query("DELETE FROM search_history")
    suspend fun clearAllSearchHistory()

    @Query("DELETE FROM search_history WHERE history = :query")
    suspend fun clearSearchHistory(query: String)

    /**
     * 查询x条数据（倒序）
     */
    @Query("SELECT * FROM search_history ORDER BY id DESC LIMIT :limit")
    fun getLocalHistory(limit: Int): LiveData<List<SearchHistory>>

}