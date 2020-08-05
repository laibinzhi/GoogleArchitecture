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
    var databaseId: Int,
    var articleType: Int,
    @field:SerializedName("id")
    var id: Int,
    @field:SerializedName("title")
    var title: String,
    @field:SerializedName("apkLink")
    var apkLink: String,
    @field:SerializedName("audit")
    var audit: Int,
    @field:SerializedName("author")
    var author: String,
    @field:SerializedName("canEdit")
    var canEdit: Boolean,
    @field:SerializedName("chapterId")
    var chapterId: Int,
    @field:SerializedName("chapterName")
    var chapterName: String,
    @field:SerializedName("collect")
    var collect: Boolean,
    @field:SerializedName("courseId")
    var courseId: Int,
    @field:SerializedName("desc")
    var desc: String,
    @field:SerializedName("descMd")
    var descMd: String,
    @field:SerializedName("envelopePic")
    var envelopePic: String,
    @field:SerializedName("fresh")
    var fresh: Boolean,
    @field:SerializedName("link")
    var link: String,
    @field:SerializedName("niceDate")
    var niceDate: String,
    @field:SerializedName("niceShareDate")
    var niceShareDate: String,
    @field:SerializedName("origin")
    var origin: String,
    @field:SerializedName("prefix")
    var prefix: String,
    @field:SerializedName("projectLink")
    var projectLink: String,
    @field:SerializedName("publishTime")
    var publishTime: Long,
    @field:SerializedName("realSuperChapterId")
    var realSuperChapterId: Int,
    @field:SerializedName("selfVisible")
    var selfVisible: Int,
    @field:SerializedName("shareDate")
    var shareDate: Long,
    @field:SerializedName("shareUser")
    var shareUser: String,
    @field:SerializedName("superChapterId")
    var superChapterId: Int,
    @field:SerializedName("superChapterName")
    var superChapterName: String,
    @field:SerializedName("type")
    var type: Int,
    @field:SerializedName("userId")
    var userId: Int,
    @field:SerializedName("visible")
    var visible: Int,
    @field:SerializedName("zan")
    var zan: Int,
    @field:SerializedName("tags")
    var tags: List<Tags>
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