package com.lbz.googlearchitecture.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-08-12 17:30
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleTypeConverter {

    @TypeConverter
    fun stringToObject(value: String): List<Article> {
        val listType = object : TypeToken<List<Article>>() {
        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<Article>): String {
        return Gson().toJson(list)
    }

}