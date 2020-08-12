package com.lbz.googlearchitecture.ui.navigation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.lbz.googlearchitecture.databinding.ItemTreeSystemBinding
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.model.NavigationResponse
import com.lbz.googlearchitecture.utils.toHtml

/**
 * @author: laibinzhi
 * @date: 2020-08-05 12:09
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class NavigationAdapter(private var datas: List<NavigationResponse>) :
    RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {

    fun setData(systems: List<NavigationResponse>) {
        datas = systems
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTreeSystemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: NavigationResponse) {
            binding.itemSystemTitle.text = item.name.toHtml()
            binding.itemSystemRv.run {
                val foxayoutManager: FlexboxLayoutManager by lazy {
                    FlexboxLayoutManager(context).apply {
                        flexDirection = FlexDirection.ROW
                        justifyContent = JustifyContent.FLEX_START
                    }
                }
                layoutManager = foxayoutManager
                setHasFixedSize(true)
                setItemViewCacheSize(200)
                isNestedScrollingEnabled = false
                adapter = NavigationChilderAdapter(item.articles, listener).apply {

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemTreeSystemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindData(datas[position])
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun toDetail(data: Article)
    }

}