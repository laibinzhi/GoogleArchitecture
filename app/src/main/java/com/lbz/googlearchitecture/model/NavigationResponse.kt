package com.lbz.googlearchitecture.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lbz.googlearchitecture.utils.ArticleTypeConverter

/**
 * @author: laibinzhi
 * @date: 2020-08-12 17:14
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "navigation_response")
@TypeConverters(ArticleTypeConverter::class)
data class NavigationResponse(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "databaseId")
    val databaseId: Int,
    @ColumnInfo(name = "articles")
    var articles: List<Article>,
    @ColumnInfo(name = "cid")
    var cid: Int,
    @ColumnInfo(name = "name")
    var name: String


) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NavigationResponse

        if (articles != other.articles) return false
        if (cid != other.cid) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = articles.hashCode()
        result = 31 * result + cid
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Navigation(articles=$articles, cid=$cid, name='$name')"
    }


}