package com.lbz.googlearchitecture.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lbz.googlearchitecture.databinding.HomeBannerBinding
import com.lbz.googlearchitecture.model.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator

/**
 * @author: laibinzhi
 * @date: 2020-07-21 20:08
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class BannerViewHolder(val binding: HomeBannerBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(banners: List<Banner>) {
        binding.banner.setAdapter(object : BannerImageAdapter<Banner>(banners) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: Banner,
                position: Int,
                size: Int
            ) {
                holder?.let {
                    Glide.with(it.imageView)
                        .load(data.imagePath)
                        .into(it.imageView)
                }

            }

        }).indicator = CircleIndicator(binding.root.context)

    }

    companion object {
        fun create(parent: ViewGroup): BannerViewHolder {
            return BannerViewHolder(
                HomeBannerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}