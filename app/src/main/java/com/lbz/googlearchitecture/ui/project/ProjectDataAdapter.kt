package com.lbz.googlearchitecture.ui.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.lbz.googlearchitecture.databinding.ProjectDataItemLayoutBinding
import com.lbz.googlearchitecture.model.ProjectData
import com.lbz.googlearchitecture.ui.base.BasePagingDataAdapter

/**
 * @author: laibinzhi
 * @date: 2020-07-16 18:46
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class ProjectDataAdapter :
    BasePagingDataAdapter<ProjectData, ProjectDataAdapter.ViewHolder>(REPO_COMPARATOR) {


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ProjectData>() {
            override fun areItemsTheSame(oldItem: ProjectData, newItem: ProjectData): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProjectData, newItem: ProjectData): Boolean =
                oldItem == newItem
        }
    }


    class ViewHolder(val binding: ProjectDataItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProjectData?) {
            binding.project = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProjectDataItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun funBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}