package com.nikulin.lines.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nikulin.lines.presentation.localcomposition.LocalNavigator
import com.nikulin.lines.presentation.navigation.AppNavigator
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun SplashRoute(
    state: SplashScreenState,
    effects: SharedFlow<SplashScreenEffect>,
    sendAction: suspend (SplashScreenAction) -> Unit
) {
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            handleEffect(effect, navigator)
        }
    }

    LaunchedEffect(Unit) {
        sendAction(SplashScreenAction.CheckLines)
    }

    SplashScreen(state)

}

@Composable
private fun SplashScreen(
    state: SplashScreenState
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            LinearProgressIndicator()
        }

    }
}

private suspend fun handleEffect(effect: SplashScreenEffect, navigator: AppNavigator) {
    when (effect) {
        SplashScreenEffect.OpenUploadScreen -> {
            navigator.openUpload()
        }

        SplashScreenEffect.OpenMainScreen -> {
            navigator.openMain()
        }
    }
}