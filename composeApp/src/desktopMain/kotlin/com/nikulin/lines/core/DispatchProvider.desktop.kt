package com.nikulin.lines.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext

actual class DispatchProvider {
    @OptIn(DelicateCoroutinesApi::class)
    actual fun io(): CoroutineDispatcher {
        return newFixedThreadPoolContext(100, "io")
    }
}