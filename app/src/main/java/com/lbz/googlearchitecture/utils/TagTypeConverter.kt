package com.lbz.googlearchitecture.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lbz.googlearchitecture.model.Tags

/**
 * @author: laibinzhi
 * @date: 2020-07-13 19:48
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

class TagTypeConverter {

    @TypeConverter
    fun stringToObject(value: String): List<Tags> {
        val listType = object : TypeToken<List<Tags>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Tags>): String {
        return Gson().toJson(list)
    }
}
