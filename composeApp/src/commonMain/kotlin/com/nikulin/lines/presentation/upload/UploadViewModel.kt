package com.nikulin.lines.presentation.upload

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.core.FileStructureParser
import com.nikulin.lines.core.LinesParser
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import com.nikulin.lines.domain.repositories.FileStructureRepository
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider,
    private val linesParser: LinesParser,
    private val fileStructureParser: FileStructureParser,
    private val fileStructureRepository: FileStructureRepository,
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
                            .map {
                                parseAndSaveFileStructure(file)
                            }
                            .map {
                                sendEffect(UploadScreenEffect.OpenMainScreen)
                            }
                    }
                }
            }
        }
    }

    private suspend fun parseAndSaveFileStructure(file: PlatformFile): Result<Unit> {
        return withContext(ioDispatcher) {
            val fileStructure = fileStructureParser.parseStructure(file)
            fileStructureRepository.saveStructure(fileStructure)
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

    private suspend fun parseAndSaveLines(file: PlatformFile, language: Language): Result<Unit> {
        val lines = linesParser.parseLines(file, language)
        return saveLines(lines)
    }

    private suspend fun saveLines(lines: List<Line>): Result<Unit> {
        return withContext(ioDispatcher) {
            linesRepository.saveLines(lines)
        }

    }
}