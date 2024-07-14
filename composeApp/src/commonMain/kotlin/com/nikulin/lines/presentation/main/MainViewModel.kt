package com.nikulin.lines.presentation.main

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider
) : BaseViewModel<MainScreenState, MainScreenEffects, MainScreenAction>(MainScreenState()) {

    private val ioDispatcher = dispatchProvider.io()
    private var languages: List<Language>? = null

    override fun reduce(action: MainScreenAction) {
        viewModelScope.launch {
            when (action) {
                MainScreenAction.Initialize -> {
                    loadLanguages()
                    loadLines()
                }
            }
        }
    }

    private suspend fun loadLanguages() {
        withContext(ioDispatcher) {
            linesRepository.getLanguages()
        }
            .onFailure {

            }
            .onSuccess {
                languages = it
            }
    }

    private suspend fun loadLines() {
        withContext(ioDispatcher) {
            linesRepository.getLines()
        }
            .onFailure {

            }
            .onSuccess { lines ->
                val currentLanguages = languages
                if (!currentLanguages.isNullOrEmpty()) {
                    screenState.applyLanguagesAndLines(currentLanguages, lines)
                }
            }
    }
}