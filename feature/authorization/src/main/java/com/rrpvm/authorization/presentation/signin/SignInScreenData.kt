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
        return PasswordData("kirill pidoras",mPassword, mPasswordVisible, mPasswordIcon)
    }
    fun loginData() : LoginData{
        return LoginData("incorrect password",this.mUsername)
    }
}

data class PasswordData(
    val errorText : String? = null,
    val mPassword: String,
    val mPasswordVisible: Boolean,
    @DrawableRes val mPasswordIcon: Int = R.drawable.ic_not_visible
)

data class LoginData(
    val errorText : String? = null,
    val mUsername: String,
)