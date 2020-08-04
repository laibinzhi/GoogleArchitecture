package com.lbz.googlearchitecture.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentSearchBinding
import com.lbz.googlearchitecture.model.Hotkey
import com.lbz.googlearchitecture.model.SearchHistory
import com.lbz.googlearchitecture.ui.MainActivity
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.home.ArticlesitoriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-23 10:13
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

//    @Inject
//    lateinit var searchViewModelFactory: SearchViewModelFactory
//    private lateinit var viewModel: SearchViewModel

    private val viewModel: SearchViewModel by viewModels()


    private val hotKeyAdapter: SearcHotKeyAdapter by lazy { SearcHotKeyAdapter(arrayListOf()) }

    private val historyAdapter: SearcHistoryAdapter by lazy { SearcHistoryAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.fragment_search

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        initHotKeyRecyclerView()
        initSearchHistoryRecyclerView()
    }

    override fun createObserver() {
//        viewModel = ViewModelProvider(this, searchViewModelFactory)
//            .get(SearchViewModel::class.java)
        viewModel.hotkeys.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                hotKeyAdapter.setData(it)
            }
        })
        viewModel.searchHistory.observe(viewLifecycleOwner, Observer {
            historyAdapter.setData(it)
        })
    }

    override fun lazyLoadData() {
        viewModel.getHotKeys()
    }

    override fun initListener() {
        binding.searchClear.setOnClickListener {
            activity?.let {
                MaterialDialog(it)
                    .cancelable(false)
                    .lifecycleOwner(this)
                    .show {
                        title(text = getString(R.string.delete_search_history_dialog_title))
                        message(text = getString(R.string.delete_search_history_dialog_message))
                        negativeButton(text = getString(R.string.delete_search_history_dialog_negative_btn_text))
                        positiveButton(text = getString(R.string.delete_search_history_dialog_positive_btn_text)) {
                            viewModel.clearAllHistory()
                        }
                    }
            }
        }
    }

    private fun initToolbar() {
        setHasOptionsMenu(true)
        (activity as MainActivity).setSupportActionBar(binding.toolbar)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initHotKeyRecyclerView() {
        val layoutManager = FlexboxLayoutManager(context)
        //设置主轴排列方式
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.hotkeyRecyclerview.layoutManager = layoutManager
        binding.hotkeyRecyclerview.adapter = hotKeyAdapter
        hotKeyAdapter.setOnItemClickListener(object : SearcHotKeyAdapter.OnItemClickListener {
            override fun onClick(data: Hotkey) {
                viewModel.insertHistory(data.name)
                navigateToSearchResult(data.name)
            }

        })
    }

    private fun initSearchHistoryRecyclerView() {
        binding.historyRecyclerview.layoutManager = LinearLayoutManager(context)
        binding.historyRecyclerview.adapter = historyAdapter
        historyAdapter.setOnItemClickListener(object : SearcHistoryAdapter.OnItemClickListener {

            override fun onCleanSearchHistory(searchHistory: SearchHistory) {
                viewModel.clearSearchHistory(searchHistory.history)
            }

            override fun searchHistory(data: SearchHistory) {
                viewModel.insertHistory(data.history)
                navigateToSearchResult(data.history)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run {
            maxWidth = Integer.MAX_VALUE
            //初始是否已经是展开的状态,写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
            onActionViewExpanded()
            queryHint = getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.insertHistory(it)
                        navigateToSearchResult(it)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            //右边是否展示搜索图标
            isSubmitButtonEnabled = true
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_baseline_search_24)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun navigateToSearchResult(search: String) {
        val direction =
            SearchFragmentDirections.actionFragmentSearchToFragmentSearchResult(search)
        findNavController().navigate(direction)
    }

    companion object {
        private const val TAG = "SearchFragment"
    }

}