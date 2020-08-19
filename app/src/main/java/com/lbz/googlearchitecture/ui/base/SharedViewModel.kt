package com.lbz.googlearchitecture.ui.base

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.lbz.googlearchitecture.model.User

/**
 * @author: laibinzhi
 * @date: 2020-07-25 17:39
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
class SharedViewModel @ViewModelInject constructor() : ViewModel() {

    var user: UnPeekLiveData<User> = UnPeekLiveData.Builder<User>()
        .setAllowNullValue(true)
        .create()

}