package com.lbz.googlearchitecture.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lbz.googlearchitecture.model.SystemChildren

/**
 * @author: laibinzhi
 * @date: 2020-07-13 19:48
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

class SystemChildrenTypeConverter {

    @TypeConverter
    fun stringToObject(value: String): List<SystemChildren> {
        val listType = object : TypeToken<List<SystemChildren>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<SystemChildren>): String {
        return Gson().toJson(list)
    }
}
