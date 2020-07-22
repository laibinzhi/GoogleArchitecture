package com.lbz.googlearchitecture.ui.project

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentOneProjectBinding
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.base.BaseLoadStateAdapter
import com.lbz.googlearchitecture.widget.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-20 15:17
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

const val CID_BUNDLE_KEY: String = "cid"

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OneProjectFragment : BaseFragment<FragmentOneProjectBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var projectViewModelFactory: ProjectViewModelFactory
    private lateinit var viewModel: ProjectViewModel
    private val adapter = ProjectDataAdapter()
    private var searchJob: Job? = null
    private var isrefreshByHand: Boolean = false
    private var cid: Int = 294

    override fun layoutId() = R.layout.fragment_one_project


    override fun initView(savedInstanceState: Bundle?) {
        arguments?.let {
            cid = it.getInt(CID_BUNDLE_KEY)
        }
        initRecycleView()
        initSwipeRefreshLayout()
        initAdapter()
    }

    override fun lazyLoadData() {
        viewModel = ViewModelProvider(this, projectViewModelFactory)
            .get(ProjectViewModel::class.java)
        getProject()
    }

    override fun createObserver() {

    }

    override fun onRefresh() {
        isrefreshByHand = true
        adapter.refresh()
    }

    override fun initListener() {
        binding.retryButton.setOnClickListener { adapter.retry() }
    }

    private fun initRecycleView() {
        binding.list.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //第一个能看的item position
                val positonCanSee =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                //如果是停止滑动状态并且不是位于第一项
                if (newState == 0 && positonCanSee != 0) {
                    binding.fab.visibility = View.VISIBLE
                } else {
                    binding.fab.visibility = View.INVISIBLE
                }
            }
        })
        binding.fab.setOnClickListener {
            binding.list.scrollToPosition(0)
            binding.fab.visibility = View.INVISIBLE
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        )
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initAdapter() {
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { adapter.retry() },
            footer = BaseLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
//            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.swipeRefreshLayout.isRefreshing =
                loadState.refresh is LoadState.Loading && isrefreshByHand
            binding.progressBar.isVisible =
                loadState.refresh is LoadState.Loading && !isrefreshByHand
            binding.retryButton.isVisible = loadState.refresh is LoadState.Error
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getProject() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getProjectData(cid).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        fun newInstance(cid: Int): OneProjectFragment {
            val args = Bundle()
            args.putInt(CID_BUNDLE_KEY, cid)
            val fragment = OneProjectFragment()
            fragment.arguments = args
            return fragment
        }
    }

}