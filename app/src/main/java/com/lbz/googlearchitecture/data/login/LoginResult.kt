package com.lbz.googlearchitecture.data.login

import com.lbz.googlearchitecture.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val user: User? = null,
    val error: String? = null
)