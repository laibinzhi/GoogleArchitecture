package com.lbz.googlearchitecture.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lbz.googlearchitecture.data.login.LoginRepository
import javax.inject.Inject

/**
 * @author: laibinzhi
 * @date: 2020-07-10 23:36
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class LoginViewModelFactory @Inject constructor(private val repository: LoginRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}