package com.nikulin.lines.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.nikulin.lines.presentation.main.MainRoute
import com.nikulin.lines.presentation.splash.SplashRoute
import com.nikulin.lines.presentation.splash.SplashViewModel
import com.nikulin.lines.presentation.upload.UploadRoute
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.appNavGraph() {

    composable(
        route = Destinations.Splash.route) {

        val viewModel: SplashViewModel = koinNavViewModel<SplashViewModel>()
        val screenState = viewModel.screenState

        SplashRoute(
            state = screenState,
            effects = viewModel.effects,
            sendAction = viewModel::sendAction
        )
    }

    composable(
        route = Destinations.Main.route
    ) {
        MainRoute()
    }

    composable(
        route = Destinations.Upload.route
    ) {

        UploadRoute()
    }
}
