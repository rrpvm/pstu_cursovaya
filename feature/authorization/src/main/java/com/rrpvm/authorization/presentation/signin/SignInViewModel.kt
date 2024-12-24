package com.rrpvm.authorization.presentation.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.model.AuthenticationModel
import com.rrpvm.domain.service.IAuthenticationService
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authenticationService: IAuthenticationService
) : ViewModel() {
    private val _isLoadingState = MutableStateFlow<Boolean>(false)
    val isLoadingState = _isLoadingState.asStateFlow()



    fun onSignInClicked(){
        viewModelScope.launch {
            _isLoadingState.value = true
            delay(3000L)
            _isLoadingState.value = false
        }
    }
    init {
        viewModelScope.launch {
            authenticationService.authenticate(AuthenticationModel("123", "123"))
        }
    }
}