package com.rrpvm.authorization.presentation.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.authorization.R
import com.rrpvm.core.TAG
import com.rrpvm.domain.model.AuthenticationModel
import com.rrpvm.domain.service.IAuthenticationService
import com.rrpvm.domain.validator.AuthorizationFieldsValidator
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService,
    private val validator: AuthorizationFieldsValidator,
) : ViewModel() {
    private val signInCoroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i(TAG, coroutineContext.toString() + " " + throwable.cause.toString())
            throwable.printStackTrace()
        }
    private val _isLoadingState = MutableStateFlow(false)
    val isLoadingState = _isLoadingState.asStateFlow()
    private val _isPasswordVisible = MutableStateFlow(false)
    private val _userPassword = MutableStateFlow("")
    private val _usernameInput = MutableStateFlow("")
    private val _userPasswordError = MutableStateFlow<String?>(null)
    private val _userLoginError = MutableStateFlow<String?>(null)
    val userPasswordError = _userPasswordError.asStateFlow()
    val userLoginError = _userLoginError.asStateFlow()
    val isSignInButtonEnabled = _userPasswordError.combine(userLoginError) { f1, f2 ->
        return@combine f1 == null && f2 == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), true)
    val dataScreen =
        combine(
            flow = _isLoadingState,
            flow2 = _isPasswordVisible,
            flow3 = _userPassword,
            flow4 = _usernameInput,
        ) { isSignInJob, isPasswordVisible, password, usernameInput ->
            SignInScreenData(
                mUsername = usernameInput,
                mPassword = password,
                mPasswordIcon = if (isPasswordVisible) R.drawable.ic_visible else R.drawable.ic_not_visible,
                mPasswordVisible = isPasswordVisible
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SignInScreenData())

    private var signJob: Job? = null


    fun onSignInClicked() {
        if (signJob?.isActive == true) {
            return
        }
        if (!onValidate()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO + signInCoroutineExceptionHandler) {
            _isLoadingState.value = true
            authenticationService.authenticate(
                AuthenticationModel(
                    username = _usernameInput.value,
                    password = _userPassword.value
                )
            )
        }.invokeOnCompletion {
            _isLoadingState.value = false
        }
    }

    fun onPasswordVisibilityToggle() {
        _isPasswordVisible.value = !_isPasswordVisible.value
    }

    fun onPasswordInput(newText: String) {
        _userPassword.value = newText
        _userPasswordError.value = null
    }

    fun onUsernameInput(username: String) {
        _usernameInput.value = username
        _userLoginError.value = null
    }

    private fun onValidate(): Boolean {
        if (!validator.validateUsername(_usernameInput.value)) {
            _userLoginError.value = "incorrect username"
            return false
        }
        if (!validator.validatePassword(_userPassword.value)) {
            _userPasswordError.value = "incorrect password"
            return false
        }
        return true
    }
}