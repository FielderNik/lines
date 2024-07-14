package com.nikulin.lines.presentation.splash

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val linesRepository: LinesRepository,
    private val dispatchProvider: DispatchProvider
) : BaseViewModel<SplashScreenState, SplashScreenEffect, SplashScreenAction>(SplashScreenState()) {

    private val ioDispatcher = dispatchProvider.io()

    override fun reduce(action: SplashScreenAction) {
        when (action) {
            SplashScreenAction.CheckLines -> {
                checkLines()
            }
        }
    }

    private fun checkLines() {
        viewModelScope.launch {
            screenState.setLoadingState()
            withContext(ioDispatcher) {
                delay(1000)
                linesRepository.hasLines()
            }
                .onFailure {
                    screenState.setUsualState()
                }
                .onSuccess { hasLines ->
                    screenState.setUsualState()
                    if (hasLines) {
                        sendEffect(SplashScreenEffect.OpenMainScreen)
                    } else {
                        sendEffect(SplashScreenEffect.OpenUploadScreen)
                    }
                }
        }
    }


}