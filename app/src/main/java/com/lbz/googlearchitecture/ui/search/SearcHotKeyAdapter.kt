package com.lbz.googlearchitecture.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.SearchHotKeyItemLayoutBinding
import com.lbz.googlearchitecture.model.Hotkey

/**
 * @author: laibinzhi
 * @date: 2020-07-23 14:53
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
private const val TAG = "SearcHotKeyAdapter"

class SearcHotKeyAdapter(private var hotKeys:List<Hotkey>) : RecyclerView.Adapter<SearcHotKeyAdapter.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    fun setData(list: List<Hotkey>) {
        hotKeys = list
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: SearchHotKeyItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Hotkey) {
            binding.hotkey = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchHotKeyItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = hotKeys.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hotKeys[position])
        holder.binding.tag.setOnClickListener {
            listener.onClick(hotKeys[position])
        }
    }

    interface OnItemClickListener {
        fun onClick(data: Hotkey)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


}