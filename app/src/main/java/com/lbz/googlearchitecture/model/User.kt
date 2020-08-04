package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lbz.googlearchitecture.utils.IntegerConverter
import com.lbz.googlearchitecture.utils.TagTypeConverter

/**
 * @author: laibinzhi
 * @date: 2020-07-24 18:34
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "user")
@TypeConverters(IntegerConverter::class)
data class User(
    @PrimaryKey
    val id: Int,
    val admin: Boolean,
//    val chapterTops: List<String>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)