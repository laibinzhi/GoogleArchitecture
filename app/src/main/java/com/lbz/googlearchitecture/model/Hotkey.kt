package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: laibinzhi
 * @date: 2020-07-22 16:51
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "hotkey")
data class Hotkey(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
) {
    override fun toString(): String {
        return "Hotkey(id=$id, link='$link', name='$name', order=$order, visible=$visible)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Hotkey

        if (id != other.id) return false
        if (link != other.link) return false
        if (name != other.name) return false
        if (order != other.order) return false
        if (visible != other.visible) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + link.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + order
        result = 31 * result + visible
        return result
    }


}