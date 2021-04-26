package com.lbz.googlearchitecture.ui.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentHomeBinding
import com.lbz.googlearchitecture.model.Article
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.base.BaseLoadStateAdapter
import com.lbz.googlearchitecture.ui.main.MainFragmentDirections
import com.lbz.googlearchitecture.utils.CacheUtil
import com.lbz.googlearchitecture.widget.SpaceItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.article_common_list_layout.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    private val viewModel: ArticlesitoriesViewModel by viewModels()

    private val adapter = ArticlesAdapter(true)
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
        retry_button.setOnClickListener {
            adapter.retry()
            viewModel.getBanner()
        }
        adapter.setOnItemClickListener(object : ArticlesAdapter.OnItemClickListener {

            override fun toDetail(data: Article) {
                ToastUtils.showShort("打开url" + data.link)
            }

            override fun collect(data: Article, imageView: ImageView) {
                if (CacheUtil.isLogin()) {
                    ToastUtils.showShort("点赞")
                } else {
                    val direction =
                        MainFragmentDirections
                            .actionFragmentMainToFragmentLogin()
                    findNavController().navigate(direction)
                }
            }

        })
    }


    private fun initToolbar() {
        toolbar.run {
            title = getString(R.string.title_home)
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        val direction =
                            MainFragmentDirections.actionFragmentMainToFragmentSearch()
                        findNavController().navigate(direction)
                    }
                }
                true
            }
        }
    }

    private fun initRecycleView() {
        list.addItemDecoration(
            SpaceItemDecoration(
                0,
                ConvertUtils.dp2px(8f),
                firstNeedTop = false
            )
        )
        list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!list.canScrollVertically(-1)) {
                    fab.visibility = View.INVISIBLE
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //是否显示第一项目
                val positonCanSee =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                //如果是停止滑动状态并且不是位于第一项
                if (newState == 0 && positonCanSee != 0) {
                    fab.visibility = View.VISIBLE
                } else {
                    fab.visibility = View.INVISIBLE
                }
            }
        })
        fab.setOnClickListener {
            list.scrollToPosition(0)
            fab.visibility = View.INVISIBLE
        }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryDark
        )
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun initAdapter() {
        list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter { adapter.retry() },
            footer = BaseLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
//            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            swipeRefreshLayout.isRefreshing =
                loadState.refresh is LoadState.Loading && isrefreshByHand
            progress_bar.isVisible =
                loadState.refresh is LoadState.Loading && !isrefreshByHand
            retry_button.isVisible = loadState.refresh is LoadState.Error
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