package com.lbz.googlearchitecture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentArticleBinding
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
class ArticleFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: ArticlesitoriesViewModel
    private val adapter = ArticlesAdapter()
    private var searchJob: Job? = null
    private var isrefreshByHand: Boolean = false

    @Inject
    lateinit var iewModelFactory: ViewModelFactory

    private fun getArticles() {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.getArticles().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_article,
            container,
            false
        )

        viewModel = ViewModelProvider(this, iewModelFactory)
            .get(ArticlesitoriesViewModel::class.java)

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)

        initSwipeRefreshLayout()
        initAdapter()
        getArticles()
        binding.retryButton.setOnClickListener { adapter.retry() }

        return binding.root
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
            header = ArticlesLoadStateAdapter { adapter.retry() },
            footer = ArticlesLoadStateAdapter { adapter.retry() }
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

    override fun onRefresh() {
        isrefreshByHand = true
        adapter.refresh()
    }

}