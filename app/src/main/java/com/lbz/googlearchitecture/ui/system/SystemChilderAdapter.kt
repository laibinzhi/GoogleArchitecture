package com.lbz.googlearchitecture.ui.system

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ItemTreeSystemChildrenBinding
import com.lbz.googlearchitecture.model.SystemChildren

/**
 * @author: laibinzhi
 * @date: 2020-08-05 14:39
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class SystemChilderAdapter(
    private val childrens: List<SystemChildren>,
    private val listener: SystemAdapter.OnItemClickListener
) :
    RecyclerView.Adapter<SystemChilderAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ItemTreeSystemChildrenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: SystemChildren) {
            binding.flowTag.text = item.name
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