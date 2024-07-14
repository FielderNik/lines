package com.nikulin.lines.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


interface AppNavigator {
    val navController: NavHostController

    @get:Composable
    val currentDestination: NavDestination?

    fun openUpload()
    fun openMain()
}

class AppNavigatorImpl(
    override val navController: NavHostController
) : AppNavigator {

    override val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    override fun openUpload() {
        navController.popNavigate(
            from = Destinations.Splash.route,
            to = Destinations.Upload.route
        )
    }
    override fun openMain() {
        navController.popNavigate(
            from = Destinations.Splash.route,
            to = Destinations.Main.route
        )
    }
}

@Composable
fun rememberAppNavigator(
    navHostController: NavHostController = rememberNavController()
): AppNavigator {
    return remember {
        AppNavigatorImpl(navHostController)
    }
}

fun NavController.popNavigate(from: String, to: String) {
    navigate(to) {
        popUpTo(from) {
            this.inclusive = true
        }
    }
}