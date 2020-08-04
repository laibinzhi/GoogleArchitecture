package com.lbz.googlearchitecture.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lbz.googlearchitecture.model.User

/**
 * @author: laibinzhi
 * @date: 2020-07-24 18:35
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user ")
    fun getUser(): LiveData<User>

    @Query("DELETE FROM user")
    suspend fun deleteUser()

    @Update
    suspend fun updateUser(user: User)
}