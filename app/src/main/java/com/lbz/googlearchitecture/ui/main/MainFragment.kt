package com.lbz.googlearchitecture.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentMainBinding

/**
 * @author: laibinzhi
 * @date: 2020-07-15 15:26
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        initViewPager2()
        initBottomNavigationView()
        return binding.root
    }

    private fun initViewPager2() {
        binding.viewpager2.adapter = MainViewPagerAdapter(this)
        binding.viewpager2.offscreenPageLimit =
            (binding.viewpager2.adapter as MainViewPagerAdapter).itemCount
        binding.viewpager2.isUserInputEnabled = false//禁止滑动
        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
    }

    private fun initBottomNavigationView() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.fragment_home -> binding.viewpager2.setCurrentItem(0,false)
                R.id.fragment_project -> binding.viewpager2.setCurrentItem(1,false)
                R.id.fragment_mine -> binding.viewpager2.setCurrentItem(2,false)
            }
            true
        }

    }

}