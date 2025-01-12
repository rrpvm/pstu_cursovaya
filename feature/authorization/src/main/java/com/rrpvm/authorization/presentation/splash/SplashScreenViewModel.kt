package com.rrpvm.authorization.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rrpvm.domain.service.IAuthenticationService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(private val authService: IAuthenticationService) : ViewModel(), ISplashScreenViewModel {
    private val _viewEffects = MutableSharedFlow<SplashScreenViewEffect>()
    val viewEffects = _viewEffects.asSharedFlow()
    override fun onSignInButtonClicked() {
        viewModelScope.launch {
            _viewEffects.emit(SplashScreenViewEffect.SignInClicked)
        }
    }

    override fun onSignInAsGuest() {
        viewModelScope.launch(Dispatchers.IO) {
            authService.authenticateAsGuest()
        }
    }
}