package com.lbz.googlearchitecture.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:20
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.title)

    private var article: Article? = null

    init {
        view.setOnClickListener {
            article?.link?.let { link ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(article: Article) {
        showArticleData(article)
    }

    private fun showArticleData(article: Article) {
        this.article = article
        title.text = article.title
    }

    companion object {
        fun create(parent: ViewGroup): ArticleViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_view_item, parent, false)
            return ArticleViewHolder(view)
        }
    }
}