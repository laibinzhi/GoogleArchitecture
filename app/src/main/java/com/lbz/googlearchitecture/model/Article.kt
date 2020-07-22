package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.lbz.googlearchitecture.utils.TagTypeConverter

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:10
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "articles")
@TypeConverters(TagTypeConverter::class)
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
    @field:SerializedName("tags")
    val tags: List<Tags>
) {
    override fun toString(): String {
        return "Article(id=$id, title='$title', apkLink='$apkLink', audit=$audit, author='$author', canEdit=$canEdit, chapterId=$chapterId, chapterName='$chapterName', collect=$collect, courseId=$courseId, desc='$desc', descMd='$descMd', envelopePic='$envelopePic', fresh=$fresh, link='$link', niceDate='$niceDate', niceShareDate='$niceShareDate', origin='$origin', prefix='$prefix', projectLink='$projectLink', publishTime=$publishTime, realSuperChapterId=$realSuperChapterId, selfVisible=$selfVisible, shareDate=$shareDate, shareUser='$shareUser', superChapterId=$superChapterId, superChapterName='$superChapterName', type=$type, userId=$userId, visible=$visible, zan=$zan)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        if (id != other.id) return false
        if (title != other.title) return false
        if (apkLink != other.apkLink) return false
        if (audit != other.audit) return false
        if (author != other.author) return false
        if (canEdit != other.canEdit) return false
        if (chapterId != other.chapterId) return false
        if (chapterName != other.chapterName) return false
        if (collect != other.collect) return false
        if (courseId != other.courseId) return false
        if (desc != other.desc) return false
        if (descMd != other.descMd) return false
        if (envelopePic != other.envelopePic) return false
        if (fresh != other.fresh) return false
        if (link != other.link) return false
        if (niceDate != other.niceDate) return false
        if (niceShareDate != other.niceShareDate) return false
        if (origin != other.origin) return false
        if (prefix != other.prefix) return false
        if (projectLink != other.projectLink) return false
        if (publishTime != other.publishTime) return false
        if (realSuperChapterId != other.realSuperChapterId) return false
        if (selfVisible != other.selfVisible) return false
        if (shareDate != other.shareDate) return false
        if (shareUser != other.shareUser) return false
        if (superChapterId != other.superChapterId) return false
        if (superChapterName != other.superChapterName) return false
        if (type != other.type) return false
        if (userId != other.userId) return false
        if (visible != other.visible) return false
        if (zan != other.zan) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + apkLink.hashCode()
        result = 31 * result + audit
        result = 31 * result + author.hashCode()
        result = 31 * result + canEdit.hashCode()
        result = 31 * result + chapterId
        result = 31 * result + chapterName.hashCode()
        result = 31 * result + collect.hashCode()
        result = 31 * result + courseId
        result = 31 * result + desc.hashCode()
        result = 31 * result + descMd.hashCode()
        result = 31 * result + envelopePic.hashCode()
        result = 31 * result + fresh.hashCode()
        result = 31 * result + link.hashCode()
        result = 31 * result + niceDate.hashCode()
        result = 31 * result + niceShareDate.hashCode()
        result = 31 * result + origin.hashCode()
        result = 31 * result + prefix.hashCode()
        result = 31 * result + projectLink.hashCode()
        result = 31 * result + publishTime.hashCode()
        result = 31 * result + realSuperChapterId
        result = 31 * result + selfVisible
        result = 31 * result + shareDate.hashCode()
        result = 31 * result + shareUser.hashCode()
        result = 31 * result + superChapterId
        result = 31 * result + superChapterName.hashCode()
        result = 31 * result + type
        result = 31 * result + userId
        result = 31 * result + visible
        result = 31 * result + zan
        return result
    }


}