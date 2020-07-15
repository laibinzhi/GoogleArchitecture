package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import com.google.gson.annotations.SerializedName

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:10
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("apkLink")
    val apkLink: String,
    @field:SerializedName("audit")
    val audit: Int,
    @field:SerializedName("author")
    val author: String,
    @field:SerializedName("canEdit")
    val canEdit: Boolean,
    @field:SerializedName("chapterId")
    val chapterId: Int,
    @field:SerializedName("chapterName")
    val chapterName: String,
    @field:SerializedName("collect")
    val collect: Boolean,
    @field:SerializedName("courseId")
    val courseId: Int,
    @field:SerializedName("desc")
    val desc: String,
    @field:SerializedName("descMd")
    val descMd: String,
    @field:SerializedName("envelopePic")
    val envelopePic: String,
    @field:SerializedName("fresh")
    val fresh: Boolean,
    @field:SerializedName("link")
    val link: String,
    @field:SerializedName("niceDate")
    val niceDate: String,
    @field:SerializedName("niceShareDate")
    val niceShareDate: String,
    @field:SerializedName("origin")
    val origin: String,
    @field:SerializedName("prefix")
    val prefix: String,
    @field:SerializedName("projectLink")
    val projectLink: String,
    @field:SerializedName("publishTime")
    val publishTime: Long,
    @field:SerializedName("realSuperChapterId")
    val realSuperChapterId: Int,
    @field:SerializedName("selfVisible")
    val selfVisible: Int,
    @field:SerializedName("shareDate")
    val shareDate: Long,
    @field:SerializedName("shareUser")
    val shareUser: String,
    @field:SerializedName("superChapterId")
    val superChapterId: Int,
    @field:SerializedName("superChapterName")
    val superChapterName: String,
    @field:SerializedName("type")
    val type: Int,
    @field:SerializedName("userId")
    val userId: Int,
    @field:SerializedName("visible")
    val visible: Int,
    @field:SerializedName("zan")
    val zan: Int,
    var prevKey: Int?,
    var nextKey: Int?
)