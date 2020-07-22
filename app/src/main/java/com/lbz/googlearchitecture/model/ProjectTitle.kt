package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @author: laibinzhi
 * @date: 2020-07-15 16:15
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "project_titles")
data class ProjectTitle(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("courseId")
    val courseId: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("order")
    val order: Int,
    @field:SerializedName("parentChapterId")
    val parentChapterId: Int,
    @field:SerializedName("userControlSetTop")
    val userControlSetTop: Boolean,
    @field:SerializedName("visible")
    val visible: Int
){
    override fun toString(): String {
        return "ProjectTitle(id=$id, courseId=$courseId, name='$name', order=$order, parentChapterId=$parentChapterId, userControlSetTop=$userControlSetTop, visible=$visible)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProjectTitle

        if (id != other.id) return false
        if (courseId != other.courseId) return false
        if (name != other.name) return false
        if (order != other.order) return false
        if (parentChapterId != other.parentChapterId) return false
        if (userControlSetTop != other.userControlSetTop) return false
        if (visible != other.visible) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + courseId
        result = 31 * result + name.hashCode()
        result = 31 * result + order
        result = 31 * result + parentChapterId
        result = 31 * result + userControlSetTop.hashCode()
        result = 31 * result + visible
        return result
    }


}