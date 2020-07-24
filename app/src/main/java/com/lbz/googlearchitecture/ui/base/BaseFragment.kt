package com.lbz.googlearchitecture.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.blankj.utilcode.util.KeyboardUtils

/**
 * @author: laibinzhi
 * @date: 2020-07-15 15:57
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {

    private var isFirst: Boolean = true

    //该类绑定的ViewDataBinding
    lateinit var binding: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        initListener()
        onVisible()
        initData()
    }

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    open fun initData() {}
    abstract fun createObserver()


    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            createObserver()
            lazyLoadData()
            isFirst = false
        }
    }

    abstract fun lazyLoadData()

    abstract fun initListener()

    override fun onPause() {
        super.onPause()
        KeyboardUtils.hideSoftInput(this!!.activity!!)
    }

}