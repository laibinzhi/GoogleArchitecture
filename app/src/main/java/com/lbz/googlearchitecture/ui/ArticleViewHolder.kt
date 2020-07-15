package com.lbz.googlearchitecture.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ArticleViewItemBinding
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleViewHolder(private val binding: ArticleViewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        showArticleData(article)
    }

    private fun showArticleData(article: Article) {
        binding.article = article
        binding.root.setOnClickListener {
            article?.link?.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            return ArticleViewHolder(
                ArticleViewItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}