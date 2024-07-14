package com.nikulin.lines.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class DispatchProvider {
    actual fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}