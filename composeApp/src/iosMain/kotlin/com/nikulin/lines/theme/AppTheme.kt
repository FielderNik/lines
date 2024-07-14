package com.nikulin.lines.theme

import androidx.compose.runtime.Composable

@Composable
actual fun AppTheme(
    isDarkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
) {
//    MaterialTheme(
//        colorScheme = if (isDarkTheme) darkScheme else lightScheme,
//        typography = AppTypography,
//        content = content
//    )
}