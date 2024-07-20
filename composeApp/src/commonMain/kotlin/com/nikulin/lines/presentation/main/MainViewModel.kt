package com.nikulin.lines.presentation.main

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.core.LinesParser
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider,
    private val linesParser: LinesParser,
) : BaseViewModel<MainScreenState, MainScreenEffects, MainScreenAction>(MainScreenState()) {

    private val ioDispatcher = dispatchProvider.io()
    private var languages: List<Language>? = null

    private var uploadedFile: PlatformFile? = null

    override fun reduce(action: MainScreenAction) {
        viewModelScope.launch {
            when (action) {
                MainScreenAction.Initialize -> {
                    loadLanguages()
                    loadLines()
                }

                is MainScreenAction.ApplyFile -> {
                    applyFile(action.file)
                }
                is MainScreenAction.SaveLines -> {
                    uploadedFile?.also {
                        parseAndSaveLines(it, action.language)
                    }
                }
            }
        }
    }

    private suspend fun applyFile(file: PlatformFile) {
        uploadedFile = file
    }

    private suspend fun parseAndSaveLines(file: PlatformFile, language: Language) {
        withContext(ioDispatcher) {
            val lines = linesParser.parseLines(file, language)
            linesRepository.saveLines(lines)
        }
            .onFailure {

            }
            .onSuccess {
                uploadedFile = null
                loadLanguages()
                loadLines()
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