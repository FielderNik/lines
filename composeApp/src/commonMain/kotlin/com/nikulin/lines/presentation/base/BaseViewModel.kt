package com.nikulin.lines.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Effect, Action>(initialState: State) : ViewModel() {

    val screenState = initialState

    private val _effects: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effects: SharedFlow<Effect> = _effects.asSharedFlow()

    private val actions: MutableSharedFlow<Action> = MutableSharedFlow()

    init {
        viewModelScope.launch {
            actions.collect { action ->
                reduce(action)
            }
        }
    }

    suspend fun sendEffect(effect: Effect) {
        _effects.emit(effect)
    }

    fun sendAction(action: Action) {
        viewModelScope.launch {
            actions.emit(action)
        }
    }

    abstract fun reduce(action: Action)


}