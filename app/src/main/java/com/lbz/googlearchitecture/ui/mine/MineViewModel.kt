package com.lbz.googlearchitecture.ui.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.lbz.googlearchitecture.data.mine.UserRepository
import com.lbz.googlearchitecture.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author: laibinzhi
 * @date: 2020-07-25 15:10
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class MineViewModel @ViewModelInject constructor(private val repository: UserRepository) :
    ViewModel() {

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _isLogin = MutableLiveData<Boolean>()
    private val _user = MutableLiveData<User>()

    val isLogin = repository.getUser().switchMap {
        if (it == null) {
            _isLogin.value = false
            _isLogin
        } else {
            _isLogin.value = true
            _isLogin
        }
    }

    val user = repository.getUser().switchMap {
        if (it == null) {
            _user.value = null
            _user
        } else {
            _user.value = it
            _user
        }
    }

    fun logout() {
        coroutineScope.launch {
            repository.logout()
        }
    }

}