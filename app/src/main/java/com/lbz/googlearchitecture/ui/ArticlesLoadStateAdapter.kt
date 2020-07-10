package com.lbz.googlearchitecture.ui

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:29
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticlesLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ArticlesLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: ArticlesLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ArticlesLoadStateViewHolder {
        return ArticlesLoadStateViewHolder.create(parent, retry)
    }
}