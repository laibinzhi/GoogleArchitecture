package com.lbz.googlearchitecture.data.mine

import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.utils.CacheUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-24 18:44
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class UserRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    fun getUser() = database.userDao().getUser()

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            service.logout()
            CacheUtil.setIsLogin(false)
            CacheUtil.setUser(null)
            database.userDao().deleteUser()
        }
    }

}