package com.nikulin.lines.presentation.upload

import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.nikulin.lines.core.DispatchProvider
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import com.nikulin.lines.domain.models.Translation
import com.nikulin.lines.domain.repositories.LinesRepository
import com.nikulin.lines.presentation.base.BaseViewModel
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UploadViewModel(
    private val linesRepository: LinesRepository,
    dispatchProvider: DispatchProvider,
) : BaseViewModel<UploadScreenState, UploadScreenEffect, UploadScreenAction>(UploadScreenState()) {

    private val ioDispatcher = dispatchProvider.io()
    private var uploadFile: PlatformFile? = null

    override fun reduce(action: UploadScreenAction) {
        viewModelScope.launch {
            when (action) {
                UploadScreenAction.RequestUploadFile -> {

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

    private suspend fun parseAndSaveLines(file: PlatformFile, language: Language) {
        var canParse = false
        val lines: MutableList<Line> = mutableListOf()
        var line: Line? = null

        val handler = KsoupHtmlHandler
            .Builder()
            .onOpenTag { name, attributes, isImplied ->
                if (name == "string") {
                    val parsedKey = attributes["name"]
                    if (!parsedKey.isNullOrEmpty()) {
                        canParse = true
                        line = Line(key = parsedKey, values = mapOf())
                    }
                }
            }
            .onText { text ->
                var currentLine = line
                if (canParse && currentLine != null) {
                    currentLine = currentLine.copy(
                        values = mapOf(language to Translation(value = text))
                    )
                    lines.add(currentLine)
                    line = null
                    canParse = false
                }
            }
            .build()
        val ksoupHtmlParser = KsoupHtmlParser(handler = handler)
        val fileContent = file.readBytes().decodeToString()
        ksoupHtmlParser.write(fileContent)
        ksoupHtmlParser.end()
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