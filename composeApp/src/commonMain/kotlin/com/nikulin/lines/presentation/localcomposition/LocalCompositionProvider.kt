package com.nikulin.lines.presentation.localcomposition

import androidx.compose.runtime.staticCompositionLocalOf
import com.nikulin.lines.presentation.navigation.AppNavigator

val LocalNavigator = staticCompositionLocalOf<AppNavigator> {
    error("No AppNavigator provided")
}