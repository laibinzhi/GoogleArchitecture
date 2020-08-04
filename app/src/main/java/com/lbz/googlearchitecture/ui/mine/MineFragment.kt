package com.lbz.googlearchitecture.ui.mine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentMineBinding
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.ui.main.MainFragmentDirections
import com.lbz.googlearchitecture.utils.CacheUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: laibinzhi
 * @date: 2020-07-15 11:45
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MineFragment : BaseFragment<FragmentMineBinding>() {

    private val viewModel: MineViewModel by viewModels()


    override fun layoutId() = R.layout.fragment_mine

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.meName.text = it.username
            } else {
                binding.meName.text = "请先登录"
            }
        })

//        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
//            Log.e("UserUpdate", "在MineFragment观察it" + it)
//        })

    }

    override fun initListener() {
        binding.logout.setOnClickListener {
            viewModel.logout()
            sharedViewModel.user.postValue(null)
        }
        binding.meName.setOnClickListener {
            if (!CacheUtil.isLogin()) {
                navigationToLogin()
            }
        }
    }

    private fun navigationToLogin() {
        val direction =
            MainFragmentDirections
                .actionFragmentMainToFragmentLogin()
        findNavController().navigate(direction)
    }

}