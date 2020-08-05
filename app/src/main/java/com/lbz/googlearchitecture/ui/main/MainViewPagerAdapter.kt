package com.lbz.googlearchitecture.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lbz.googlearchitecture.ui.home.HomeFragment
import com.lbz.googlearchitecture.ui.mine.MineFragment
import com.lbz.googlearchitecture.ui.project.ProjectFragment
import com.lbz.googlearchitecture.ui.square.SquareFragment

/**
 * @author: laibinzhi
 * @date: 2020-07-15 12:00
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */

const val HOME_PAGE_INDEX = 0
const val PROJECT_PAGE_INDEX = 1
const val SQUARE_PAGE_INDEX = 2
const val MINE_PAGE_INDEX = 3


class MainViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val bottomNavigationViewFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_PAGE_INDEX to { HomeFragment() },
        PROJECT_PAGE_INDEX to { ProjectFragment() },
        SQUARE_PAGE_INDEX to { SquareFragment() },
        MINE_PAGE_INDEX to { MineFragment() }
    )

    override fun getItemCount(): Int {
        return bottomNavigationViewFragmentCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return bottomNavigationViewFragmentCreators[position]?.invoke()
            ?: throw IndexOutOfBoundsException()
    }

}