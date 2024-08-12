package com.nikulin.lines.presentation.main

import androidx.lifecycle.viewModelScope
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.core.LinesParser
import com.nikulin.lines.domain.models.Entry
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.repositories.FileStructureRepository
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider,
    private val linesParser: LinesParser,
    private val fileStructureRepository: FileStructureRepository,
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

                is MainScreenAction.UploadLanguageFile -> {
                    applyFile(action.file)
                    screenState.showLanguageDialog(languages?.any { it.isMain } == true)
                }
                is MainScreenAction.SaveLines -> {
                    uploadedFile?.also {
                        parseAndSaveLines(it, action.language)
                    }
                }

                is MainScreenAction.UnloadXml -> {
                    fillAndSaveXmlFile(action.language)
                }

                is MainScreenAction.UploadTranslateFile -> {
                    applyFile(action.file)
                    languages?.also {
                        val filteredLanguages = it.filter { language -> !language.isMain }
                        screenState.showSelectTranslateLanguageDialog(filteredLanguages)
                    }
                }

                is MainScreenAction.AddTranslatedLines -> {
                    uploadedFile?.also {
                        parseAndAddTranslateLines(it, action.language)
                    }
                }

                MainScreenAction.HideDialog -> {
                    screenState.hideDialog()
                }

                MainScreenAction.RequestUnloadXml -> {
                    languages?.also {
                        screenState.showSelectUnloadXmlLanguage(it)
                    }
                }
            }
        }
    }

    private suspend fun fillAndSaveXmlFile(language: Language) {
        val structure = fileStructureRepository.getStructure().getOrNull()
        val savedLines = screenState.lines
        val fileLines = mutableMapOf<Int, String>()
        structure?.forEach { item ->
            when (val entry = item.value) {
                is  Entry.Data -> {
                    val keyLine = entry.key
                    val line = savedLines.find { it.key == keyLine }
                    if (line != null) {
                        val lineTranslation = line.values[language]
                        if (lineTranslation != null) {
                            val newValue = "<string name=\"${entry.key}\">${lineTranslation.value}</string>"
                            fileLines[item.key] = newValue
                        }
                    }
                }
                is Entry.Empty -> {
                    fileLines[item.key] = ""
                }
                is Entry.SystemData -> {
                    fileLines[item.key] = entry.string
                }
            }
        }
        fileLines.forEach {
            println(it)
        }
        val fileContent = fileLines.values.joinToString(separator = "\n")
        writeFile(fileContent, language)
    }

    private suspend fun writeFile(fileContent: String, language: Language) {
        val file = FileKit.saveFile(
            baseName = "strings_${language.value}",
            extension = "xml",
            bytes = fileContent.encodeToByteArray()
        )
    }

    private suspend fun applyFile(file: PlatformFile) {
        uploadedFile = file
    }

    private suspend fun parseAndSaveLines(file: PlatformFile, language: Language) {
        withContext(ioDispatcher) {
            val lines = linesParser.parseLines(file, language)
            linesRepository.saveLines(lines)
        }
            .onSuccess {
                uploadedFile = null
                loadLanguages()
                loadLines()
            }
    }

    private suspend fun parseAndAddTranslateLines(file: PlatformFile, language: Language) {
        withContext(ioDispatcher) {
            val lines = linesParser.parseLines(file, language)
            linesRepository.saveLines(lines)
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