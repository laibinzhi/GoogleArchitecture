package com.lbz.googlearchitecture.ui.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.data.mine.UserRepository
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class MineViewModelFactory @Inject constructor(private val repository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MineViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MineViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}