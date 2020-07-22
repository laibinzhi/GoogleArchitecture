package com.lbz.googlearchitecture.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentHomeBinding
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
 * @date: 2020-07-15 08:41
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var iewModelFactoryArticle: ArticleViewModelFactory
    private lateinit var viewModel: ArticlesitoriesViewModel
    private val adapter = ArticlesAdapter()
    private var job: Job? = null
    private var isrefreshByHand: Boolean = false

    override fun layoutId(): Int = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initRecycleView()
        initSwipeRefreshLayout()
        initAdapter()
    }

    override fun lazyLoadData() {
        viewModel = ViewModelProvider(this, iewModelFactoryArticle)
            .get(ArticlesitoriesViewModel::class.java)
        getArticles()
        getBanner()
    }

    override fun createObserver() {

    }

    override fun onRefresh() {
        isrefreshByHand = true
        adapter.refresh()
        viewModel.getBanner()
    }

    override fun initListener() {
        binding.retryButton.setOnClickListener {
            adapter.retry()
            viewModel.getBanner()
        }
    }


    private fun initToolbar() {
        binding.toolbar.run {
            title = getString(R.string.title_home)
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        Toast.makeText(activity, "点击了搜索", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    private fun initRecycleView() {
        binding.list.addItemDecoration(
            SpaceItemDecoration(
                0,
                ConvertUtils.dp2px(8f),
                firstNeedTop = false
            )
        )
        binding.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!binding.list.canScrollVertically(-1)) {
                    binding.fab.visibility = View.INVISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //是否显示第一项目
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


    private fun getArticles() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getArticles().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun getBanner() {
        viewModel?.let { homeModel ->
            homeModel.banner.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    adapter.setBanner(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
        viewModel.getBanner()
    }

}