package com.lbz.googlearchitecture.ui.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.data.search.SearchPagingSource
import com.lbz.googlearchitecture.databinding.FragmentSearchResultBinding
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.base.BaseLoadStateAdapter
import com.lbz.googlearchitecture.ui.home.ArticlesAdapter
import com.lbz.googlearchitecture.ui.main.MainFragmentDirections
import com.lbz.googlearchitecture.utils.CacheUtil
import com.lbz.googlearchitecture.widget.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-23 17:59
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding>(),
    SwipeRefreshLayout.OnRefreshListener {

    private val args: SearchResultFragmentArgs by navArgs()
    private val viewModel: SearchViewModel by viewModels()

    private val adapter = ArticlesAdapter(false)
    private var job: Job? = null
    private var isrefreshByHand: Boolean = false


    override fun layoutId() = R.layout.fragment_search_result

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initRecycleView()
        initSwipeRefreshLayout()
        initAdapter()
    }

    override fun lazyLoadData() {
        getSearchResult()
    }

    private fun initToolbar() {
        binding.toolbar.run {
            title = args.search
            navigationIcon =
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_backspace_24)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initRecycleView() {
        binding.list.addItemDecoration(
            SpaceItemDecoration(
                0,
                ConvertUtils.dp2px(8f),
                firstNeedTop = true
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

            var isEmpty = false//是否是空数据

            if (loadState.refresh is LoadState.Error) {
                val throwable = (loadState.refresh as LoadState.Error).error
                if (throwable.message == SearchPagingSource.SEARCH_RESULT_EMPTY) {
                    isEmpty = true
                }
            }
            binding.list.isVisible = !isEmpty//第一页数据为空的时候，列表隐藏
            binding.swipeRefreshLayout.isVisible = !isEmpty

            binding.swipeRefreshLayout.isRefreshing =
                loadState.refresh is LoadState.Loading && isrefreshByHand
            binding.progressBar.isVisible =
                loadState.refresh is LoadState.Loading && !isrefreshByHand
            binding.retryButton.isVisible =
                loadState.refresh is LoadState.Error && !isEmpty
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
            //空白布局
            binding.emptyLayout.emptyLayoutRoot.isVisible = isEmpty
        }
    }

    private fun getSearchResult() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.getSearchResult(args.search).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun createObserver() {
//        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
//            Log.e("UserUpdate","在SearchResult观察it" +it)
//        })
    }

    override fun initListener() {
        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
        binding.emptyLayout.emptyLayoutRetryBtn.setOnClickListener {
            adapter.retry()
        }

        adapter.setOnItemClickListener(object : ArticlesAdapter.OnItemClickListener {

            override fun toDetail(data: Article) {
                ToastUtils.showShort("打开url"+data.link)
            }

            override fun collect(data: Article, imageView: ImageView) {
                if (CacheUtil.isLogin()) {
                    ToastUtils.showShort("点赞")
                } else {
                    val direction =
                        SearchResultFragmentDirections
                            .actionFragmentResultToFragmentLogin()
                    findNavController().navigate(direction)
                }
            }

        })
    }

    override fun onRefresh() {
        isrefreshByHand = true
        adapter.refresh()
    }
}