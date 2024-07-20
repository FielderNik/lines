package com.nikulin.lines.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import io.github.vinceglb.filekit.core.PlatformFile

class MainScreenState {
    val languages: SnapshotStateList<Language> = mutableStateListOf()
    val lines: SnapshotStateList<Line> = mutableStateListOf()

    val showLanguageDialog: MutableState<Boolean> = mutableStateOf(false)
    val showMenu: MutableState<Boolean> = mutableStateOf(false)

    val languageValue: MutableState<String?> = mutableStateOf(null)
    val isMainLanguage: MutableState<Boolean> = mutableStateOf(false)
    val hasMainLanguage: MutableState<Boolean> = mutableStateOf(false)

    fun applyLanguagesAndLines(languages: List<Language>, lines: List<Line>) {
        hasMainLanguage.value = languages.any { it.isMain }
        this.languages.clear()
        this.languages.addAll(languages)
        this.lines.clear()
        this.lines.addAll(lines)
    }
}

sealed class MainScreenEffects {

}

sealed class MainScreenAction {
    data object Initialize : MainScreenAction()
    data class ApplyFile(val file: PlatformFile): MainScreenAction()
    data class SaveLines(val language: Language) : MainScreenAction()

}