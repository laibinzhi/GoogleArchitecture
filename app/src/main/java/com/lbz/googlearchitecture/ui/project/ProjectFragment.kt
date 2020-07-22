package com.lbz.googlearchitecture.ui.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.data.project.ProjectStatus
import com.lbz.googlearchitecture.databinding.FragmentProjectBinding
import com.lbz.googlearchitecture.model.ProjectTitle
import com.lbz.googlearchitecture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-15 11:44
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProjectFragment : BaseFragment<FragmentProjectBinding>() {

    @Inject
    lateinit var projectViewModelFactory: ProjectViewModelFactory
    private lateinit var viewModel: ProjectViewModel
    private var fragments: ArrayList<Fragment> = arrayListOf()
    private var mTitleList: ArrayList<ProjectTitle> = arrayListOf()

    override fun layoutId() = R.layout.fragment_project

    override fun initView(savedInstanceState: Bundle?) {
        binding.datalayout.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.adapter = ProjectViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    override fun initListener() {
        binding.retryButton.setOnClickListener {
            viewModel.getProjectTitles()
        }
    }

    override fun lazyLoadData() {
        viewModel.getProjectTitles()
    }

    override fun createObserver() {
        viewModel = ViewModelProvider(this, projectViewModelFactory)
            .get(ProjectViewModel::class.java)
        viewModel.projectTitles.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.datalayout.visibility = View.VISIBLE
                binding.retryButton.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                fragments.clear()
                mTitleList.clear()
                mTitleList.addAll(it)
                it.forEach {
                    fragments.add(OneProjectFragment.newInstance(it.id))
                }

                binding.viewPager.adapter?.notifyDataSetChanged()
                binding.viewPager.offscreenPageLimit = fragments.size
            }

        })

        viewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ProjectStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.datalayout.visibility = View.GONE
                    binding.retryButton.visibility = View.GONE
                }
                ProjectStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.datalayout.visibility = View.VISIBLE
                    binding.retryButton.visibility = View.GONE
                }
                ProjectStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.datalayout.visibility = View.GONE
                    binding.retryButton.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun getTabTitle(position: Int) = mTitleList[position].name

    inner class ProjectViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

}