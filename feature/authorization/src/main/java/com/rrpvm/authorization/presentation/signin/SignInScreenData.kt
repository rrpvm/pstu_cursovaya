package com.rrpvm.authorization.presentation.signin

import androidx.annotation.DrawableRes
import com.rrpvm.authorization.R

data class SignInScreenData(
    val mUsername: String = "",
    val mPassword: String = "",
    val mPasswordVisible: Boolean = false,
    @DrawableRes val mPasswordIcon: Int = R.drawable.ic_not_visible,
) {
    fun passwordData(): PasswordData {
        return PasswordData(mPassword, mPasswordVisible, mPasswordIcon)
    }

    fun loginData(): LoginData {
        return LoginData(this.mUsername)
    }
}

data class PasswordData(
    val mPassword: String,
    val mPasswordVisible: Boolean,
    @DrawableRes val mPasswordIcon: Int = R.drawable.ic_not_visible
)

data class LoginData(
    val mUsername: String,
)