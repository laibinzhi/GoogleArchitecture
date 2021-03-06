package com.lbz.googlearchitecture.ui.home

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.Banner
import com.lbz.googlearchitecture.ui.base.BasePagingDataAdapter

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:28
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val TYPE_BANNER = 1
private const val TYPE_DATA = 2

/**
 *  isHome  true->首页  false->search
 */
class ArticlesAdapter(private val isHome: Boolean) :
    BasePagingDataAdapter<Article, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    private var banners: List<Banner> = emptyList()

    fun setBanner(data: List<Banner>) {
        banners = data
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHome) {
            if (position == 0) {
                TYPE_BANNER
            } else {
                TYPE_DATA
            }
        } else {
            TYPE_DATA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_BANNER) {
            BannerViewHolder.create(parent)
        } else {
            ArticleViewHolder.create(parent, this.listener)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem == newItem
        }
    }

    override fun funBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BannerViewHolder) {
            holder.bind(banners)
        } else {
            //因为在home中0这个position已经给了banner，所以position要减去1
            val pos = if (isHome) (position - 1) else position
            val repoItem = getItem(pos)
            if (repoItem != null) {
                (holder as ArticleViewHolder).bind(repoItem)
            }
        }
    }

    interface OnItemClickListener {
        fun toDetail(data: Article)
        fun collect(data: Article, imageView: ImageView)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    lateinit var listener: OnItemClickListener


}