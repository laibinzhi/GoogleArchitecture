package com.lbz.googlearchitecture.data.login

import com.lbz.googlearchitecture.api.LbzService
import com.lbz.googlearchitecture.db.LbzDatabase
import com.lbz.googlearchitecture.utils.CacheUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:18
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */


class LoginRepository @Inject constructor(
    private val service: LbzService,
    private val database: LbzDatabase
) {

    suspend fun login(username: String, password: String): LoginResult {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.login(username, password).await()
                if (response.errorCode == 0) {
                    database.userDao().insertUser(response.data)
                    CacheUtil.setUser(response.data)
                    CacheUtil.setIsLogin(true)
                    LoginResult(user = response.data)
                } else {
                    LoginResult(error = "密码错误")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                LoginResult(error = "报错了" + e.message)
            }
        }
    }

}