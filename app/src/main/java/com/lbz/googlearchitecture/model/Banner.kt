package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: laibinzhi
 * @date: 2020-07-21 19:05
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "banner")
data class Banner(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
){
    override fun toString(): String {
        return "Banner(desc='$desc', id=$id, imagePath='$imagePath', isVisible=$isVisible, order=$order, title='$title', type=$type, url='$url')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Banner

        if (desc != other.desc) return false
        if (id != other.id) return false
        if (imagePath != other.imagePath) return false
        if (isVisible != other.isVisible) return false
        if (order != other.order) return false
        if (title != other.title) return false
        if (type != other.type) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = desc.hashCode()
        result = 31 * result + id
        result = 31 * result + imagePath.hashCode()
        result = 31 * result + isVisible
        result = 31 * result + order
        result = 31 * result + title.hashCode()
        result = 31 * result + type
        result = 31 * result + url.hashCode()
        return result
    }


}