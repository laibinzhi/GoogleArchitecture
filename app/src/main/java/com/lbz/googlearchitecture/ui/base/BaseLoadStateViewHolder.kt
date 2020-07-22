package com.lbz.googlearchitecture.ui.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.ArticlesLoadStateFooterViewItemBinding

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:30
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class BaseLoadStateViewHolder(
    private val binding: ArticlesLoadStateFooterViewItemBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = loadState.error.localizedMessage
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): BaseLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.articles_load_state_footer_view_item, parent, false)
            val binding = ArticlesLoadStateFooterViewItemBinding.bind(view)
            return BaseLoadStateViewHolder(
                binding,
                retry
            )
        }
    }
}