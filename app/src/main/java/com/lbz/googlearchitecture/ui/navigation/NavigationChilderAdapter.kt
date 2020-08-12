package com.lbz.googlearchitecture.ui.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ItemTreeSystemChildrenBinding
import com.lbz.googlearchitecture.model.Article

/**
 * @author: laibinzhi
 * @date: 2020-08-05 14:39
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class NavigationChilderAdapter(
    private val childrens: List<Article>,
    private val listener: NavigationAdapter.OnItemClickListener
) :
    RecyclerView.Adapter<NavigationChilderAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemTreeSystemChildrenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: Article) {
            binding.flowTag.text = item.title
            binding.flowTag.setOnClickListener {
                listener.toDetail(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTreeSystemChildrenBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = childrens.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(childrens[position])
    }


}