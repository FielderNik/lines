package com.nikulin.lines.core

import kotlinx.coroutines.CoroutineDispatcher

expect class DispatchProvider() {
    fun io(): CoroutineDispatcher
}