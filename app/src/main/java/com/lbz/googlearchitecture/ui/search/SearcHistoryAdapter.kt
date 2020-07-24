package com.lbz.googlearchitecture.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ItemSearchHistoryBinding
import com.lbz.googlearchitecture.model.SearchHistory

/**
 * @author: laibinzhi
 * @date: 2020-07-23 16:08
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class SearcHistoryAdapter(private var searchHistoryList: List<SearchHistory>) :
    RecyclerView.Adapter<SearcHistoryAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    fun setData(list: List<SearchHistory>) {
        searchHistoryList = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchHistory) {
            binding.searchHistory = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = searchHistoryList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(searchHistoryList[position])
        holder.binding.itemHistoryDel.setOnClickListener {
            listener.onCleanSearchHistory(searchHistoryList[position])
        }
        holder.binding.itemHistoryText.setOnClickListener {
            listener.searchHistory(searchHistoryList[position])
        }
    }

    interface OnItemClickListener {
        fun onCleanSearchHistory(data: SearchHistory)
        fun searchHistory(data: SearchHistory)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}