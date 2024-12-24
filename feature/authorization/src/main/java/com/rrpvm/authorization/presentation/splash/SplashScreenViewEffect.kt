package com.rrpvm.authorization.presentation.splash

sealed class SplashScreenViewEffect {
    data object SignInClicked : SplashScreenViewEffect()
}