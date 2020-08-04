package com.lbz.googlearchitecture.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ArticleViewItemBinding
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleViewHolder(
    private val binding: ArticleViewItemBinding,
    private val listener: ArticlesAdapter.OnItemClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        showArticleData(article)
    }

    private fun showArticleData(article: Article) {
        binding.article = article
        binding.showTag = true
        binding.itemHomeCollect.setOnClickListener {
            listener.collect(article, it as ImageView)
        }
        binding.rootView.setOnClickListener {
            listener.toDetail(article)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: ArticlesAdapter.OnItemClickListener
        ): ArticleViewHolder {
            return ArticleViewHolder(
                ArticleViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }
}