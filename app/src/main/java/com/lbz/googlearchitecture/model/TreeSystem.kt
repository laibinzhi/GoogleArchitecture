package com.lbz.googlearchitecture.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lbz.googlearchitecture.utils.SystemChildrenTypeConverter

/**
 * @author: laibinzhi
 * @date: 2020-08-05 12:18
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "tree_system")
@TypeConverters(SystemChildrenTypeConverter::class)
data class TreeSystem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "databaseId")
    val databaseId: Int,
    @ColumnInfo(name = "children")
    var children: List<SystemChildren>,
    @ColumnInfo(name = "courseId")
    var courseId: Int,
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "order")
    var order: Int,
    @ColumnInfo(name = "parentChapterId")
    var parentChapterId: Int,
    @ColumnInfo(name = "userControlSetTop")
    var userControlSetTop: Boolean,
    @ColumnInfo(name = "visible")
    var visible: Int
)

data class SystemChildren(
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int


)