package com.lbz.googlearchitecture.ui.base

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:29
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class BaseLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<BaseLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: BaseLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseLoadStateViewHolder {
        return BaseLoadStateViewHolder.create(
            parent,
            retry
        )
    }
}