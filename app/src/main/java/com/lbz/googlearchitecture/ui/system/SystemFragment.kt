package com.lbz.googlearchitecture.ui.system

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.data.square.Status
import com.lbz.googlearchitecture.databinding.ArticleCommonListLayoutBinding
import com.lbz.googlearchitecture.model.SystemChildren
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.square.SquareViewModel
import com.lbz.googlearchitecture.widget.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.article_common_list_layout.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: laibinzhi
 * @date: 2020-08-04 19:04
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SystemFragment : BaseFragment<ArticleCommonListLayoutBinding>() {

    private val viewModel: SquareViewModel by viewModels()

    private val systemAdapter: SystemAdapter by lazy { SystemAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.article_common_list_layout

    private var isrefreshByHand: Boolean = false

    override fun initView(savedInstanceState: Bundle?) {
        initRecycleView()
        initSwipeRefreshLayout()
        binding.retryButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.run {
            setColorSchemeResources(
                R.color.colorPrimary,
                R.color.colorPrimaryDark
            )
            setOnRefreshListener {
                isrefreshByHand = true
                viewModel.getTreeSystems()
            }
        }
    }

    private fun initRecycleView() {
        list.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = systemAdapter
            addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
        }

    }

    override fun createObserver() {
        viewModel.treeSystems.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                binding.swipeRefreshLayout.visibility = View.VISIBLE
                binding.retryButton.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                systemAdapter.setData(it)
            }
        })

        viewModel.status.observe(viewLifecycleOwner, Observer { loadState ->
            when (loadState) {
                Status.LOADING -> {
                    binding.retryButton.visibility = View.GONE
                    if (isrefreshByHand) {
                        binding.swipeRefreshLayout.isRefreshing = true
                    } else {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
                Status.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.retryButton.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.retryButton.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }

    override fun lazyLoadData() {
        viewModel.getTreeSystems()
    }

    override fun initListener() {
        binding.retryButton.setOnClickListener {
            viewModel.getTreeSystems()
        }
        systemAdapter.setOnItemClickListener(object : SystemAdapter.OnItemClickListener {
            override fun toDetail(data: SystemChildren) {
                ToastUtils.showShort("点击了" + data.name)
            }
        })
    }


}