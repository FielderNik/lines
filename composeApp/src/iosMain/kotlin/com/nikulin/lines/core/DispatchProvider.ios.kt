package com.nikulin.lines.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

actual class DispatchProvider {
    actual fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}