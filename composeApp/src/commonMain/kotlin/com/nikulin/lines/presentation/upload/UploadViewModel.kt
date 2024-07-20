package com.nikulin.lines.presentation.upload

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.core.LinesParser
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider,
    private val linesParser: LinesParser,
) : BaseViewModel<UploadScreenState, UploadScreenEffect, UploadScreenAction>(UploadScreenState()) {

    private val ioDispatcher = dispatchProvider.io()
    private var uploadFile: PlatformFile? = null

    override fun reduce(action: UploadScreenAction) {
        viewModelScope.launch {
            when (action) {
                UploadScreenAction.Initialize -> {
                    loadHasMainLanguage()
                }

                is UploadScreenAction.ApplyFile -> {
                    uploadFile = action.file
                }

                is UploadScreenAction.SaveLines -> {
                    uploadFile?.also { file ->
                        parseAndSaveLines(file, action.language)
                    }
                }
            }
        }
    }

    private suspend fun loadHasMainLanguage() {
        withContext(ioDispatcher) {
            linesRepository.getLanguages()
        }
            .onSuccess { languages ->
                screenState.hasMainLanguage.value = languages.any { it.isMain }
            }
    }

    private suspend fun parseAndSaveLines(file: PlatformFile, language: Language) {
        val lines = linesParser.parseLines(file, language)
        saveLines(lines)
    }

    private suspend fun saveLines(lines: List<Line>) {
        withContext(ioDispatcher) {
            linesRepository.saveLines(lines)
        }
            .onFailure {

            }
            .onSuccess {
                sendEffect(UploadScreenEffect.OpenMainScreen)
            }
    }
}