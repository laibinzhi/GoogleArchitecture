package com.lbz.googlearchitecture.ui.square

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentViewpagerBinding
import com.lbz.googlearchitecture.ui.base.BaseFragment
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
 * @date: 2020-08-04 10:52
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SquareFragment :BaseFragment<FragmentViewpagerBinding>(){

    var mTitleList = arrayListOf("广场", "每日一问", "体系", "导航")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView(savedInstanceState: Bundle?) {
        binding.datalayout.visibility = View.VISIBLE
        binding.retryButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.adapter = SquareViewPagerAdapter(this)
        initMagicIndicator()
    }

    private fun initMagicIndicator() {
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mTitleList[index]
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

    override fun createObserver() {
    }

    override fun lazyLoadData() {
    }

    override fun initListener() {
    }

    inner class SquareViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = fragments.size
        override fun createFragment(position: Int) = fragments[position]
    }

}