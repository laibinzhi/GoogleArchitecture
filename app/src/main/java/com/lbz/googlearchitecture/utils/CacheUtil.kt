package com.lbz.googlearchitecture.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.lbz.googlearchitecture.model.User
import com.tencent.mmkv.MMKV

/**
 * @author: laibinzhi
 * @date: 2020-07-27 10:19
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
object CacheUtil {

    /**
     * 获取保存的账户信息
     */
    fun getUser(): User? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, User::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: User?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
        } else {
            kv.encode("user", Gson().toJson(userResponse))
        }
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login", false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin: Boolean) {
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login", isLogin)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }


}