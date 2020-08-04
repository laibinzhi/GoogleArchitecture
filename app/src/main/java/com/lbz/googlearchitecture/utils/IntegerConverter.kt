package com.lbz.googlearchitecture.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author: laibinzhi
 * @date: 2020-07-13 19:48
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

class IntegerConverter {

    @TypeConverter
    fun stringToObject(value: String): List<Integer> {
        val listType = object : TypeToken<List<Integer>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Integer>): String {
        return Gson().toJson(list)
    }
}
