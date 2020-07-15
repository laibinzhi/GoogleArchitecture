package com.lbz.googlearchitecture.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: laibinzhi
 * @date: 2020-07-11 02:31
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val articleId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
