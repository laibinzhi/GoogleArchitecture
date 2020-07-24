package com.lbz.googlearchitecture.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: laibinzhi
 * @date: 2020-07-23 15:47
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "search_history")
data class SearchHistory(
    val history: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}