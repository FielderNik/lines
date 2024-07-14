package com.nikulin.lines.theme

import androidx.compose.runtime.Composable

@Composable
expect fun AppTheme(
    isDarkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)