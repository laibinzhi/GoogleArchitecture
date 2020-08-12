package com.lbz.googlearchitecture.ui.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.data.project.ProjectStatus
import com.lbz.googlearchitecture.databinding.FragmentViewpagerBinding
import com.lbz.googlearchitecture.model.ProjectTitle
import com.lbz.googlearchitecture.ui.base.BaseFragment
import com.lbz.googlearchitecture.utils.toHtml
import com.lbz.googlearchitecture.widget.ScaleTransitionPagerTitleView
import com.lbz.googlearchitecture.widget.ViewPager2Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator

/**
 * @author: laibinzhi
 * @date: 2020-07-15 11:44
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProjectFragment : BaseFragment<FragmentViewpagerBinding>() {

    private val viewModel: ProjectViewModel by viewModels()

    private var fragments: ArrayList<Fragment> = arrayListOf()

    private var mTitleList: ArrayList<ProjectTitle> = arrayListOf()

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        binding.datalayout.visibility = View.GONE
        binding.retryButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.adapter = ProjectViewPagerAdapter(this)
        initMagicIndicator()
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
                binding.magicIndicator.navigator.notifyDataSetChanged()
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

//        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
//            Log.e("UserUpdate", "在ProjectFragment观察it" + it)
//        })

    }

    private fun initMagicIndicator() {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mTitleList[index].name.toHtml()
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener {
                        binding.viewPager.currentItem = index
                    }
                }
            }

            override fun getCount(): Int = mTitleList.size

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    //线条的宽高度
                    lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                    //线条的圆角
                    roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    //线条的颜色
                    setColors(Color.WHITE)
                }
            }

        }
        binding.magicIndicator.navigator = commonNavigator
        ViewPager2Helper.bind(binding.magicIndicator, binding.viewPager)
    }

    inner class ProjectViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

}