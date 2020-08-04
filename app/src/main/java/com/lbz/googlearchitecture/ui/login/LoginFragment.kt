package com.lbz.googlearchitecture.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.KeyboardUtils
import com.lbz.googlearchitecture.R
import com.lbz.googlearchitecture.databinding.FragmentLoginBinding
import com.lbz.googlearchitecture.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author: laibinzhi
 * @date: 2020-07-25 15:44
 * @github: https://github.com/laibinzhi
 * @blog: https://www.laibinzhi.top/
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar()
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun createObserver() {
        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer
            binding.loginBtn.isEnabled = loginState.isDataValid
        })

        viewModel.loginResult.observe(viewLifecycleOwner, Observer {
            binding.loading.visibility = View.GONE
            if (it.user != null) {
                Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show()
                sharedViewModel.user.postValue(it.user)
                findNavController().navigateUp()
            } else {
                Toast.makeText(context, "登录失败" + it.error, Toast.LENGTH_SHORT).show()
            }
        })

        binding.usernameEt.afterTextChanged {
            viewModel.loginDataChanged(
                username = binding.usernameEt.text.toString(),
                password = binding.passwordEt.text.toString()
            )
        }

        binding.passwordEt.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    binding.usernameEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.login(
                            binding.usernameEt.text.toString(),
                            binding.passwordEt.text.toString()
                        )
                }
                false
            }

            binding.loginBtn.setOnClickListener {
                binding.loading.visibility = View.VISIBLE
                KeyboardUtils.hideSoftInput(this@LoginFragment.activity!!)
                viewModel.login(
                    binding.usernameEt.text.toString(),
                    binding.passwordEt.text.toString()
                )
            }
        }
    }

    override fun lazyLoadData() {

    }

    override fun initListener() {

    }

    private fun initToolbar() {
        binding.toolbar.run {
            title = getString(R.string.login)
            navigationIcon =
                ContextCompat.getDrawable(context, R.drawable.ic_baseline_keyboard_backspace_24)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}