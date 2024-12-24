package com.rrpvm.authorization.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashScreenViewModel : ViewModel(), ISplashScreenViewModel {
    private val _viewEffects = MutableSharedFlow<SplashScreenViewEffect>()
    val viewEffects = _viewEffects.asSharedFlow()
    override fun onSignInButtonClicked() {
        viewModelScope.launch {
            _viewEffects.emit(SplashScreenViewEffect.SignInClicked)
        }
    }
}