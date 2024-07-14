package com.nikulin.lines.presentation.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SplashScreenState {
    var isLoading: Boolean by mutableStateOf(false)
        private set

    fun setLoadingState() {
        isLoading = true
    }

    fun setUsualState() {
        isLoading = false
    }
}

sealed class SplashScreenEffect {
    data object OpenUploadScreen : SplashScreenEffect()
    data object OpenMainScreen : SplashScreenEffect()
}

sealed class SplashScreenAction {
    data object CheckLines : SplashScreenAction()
}