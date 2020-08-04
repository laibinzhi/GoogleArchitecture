package com.lbz.googlearchitecture

import android.app.Application
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

/**
 * @author: laibinzhi
 * @date: 2020-07-15 08:12
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
    }
}