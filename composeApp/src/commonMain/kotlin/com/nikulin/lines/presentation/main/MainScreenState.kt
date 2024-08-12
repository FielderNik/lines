package com.nikulin.lines.presentation.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line
import io.github.vinceglb.filekit.core.PlatformFile

class MainScreenState {

    sealed class Dialog {

        data class EnterLanguage(
            val languageState: MutableState<String?> = mutableStateOf(null),
            val isMainLanguage: MutableState<Boolean> = mutableStateOf(false),
            val hasMainLanguage: Boolean
        ) : Dialog()

        data class SelectUnloadXmlLanguage(
            val languages: List<Language>,
            val selectedLanguage: MutableState<Language?> = mutableStateOf(null)
        ) : Dialog()

        data class SelectUploadTranslateLanguage(
            val languages: List<Language>,
            val selectedLanguage: MutableState<Language?> = mutableStateOf(null)
        ) : Dialog()

        data object Hide : Dialog()
    }

    val dialogState: MutableState<Dialog> = mutableStateOf(Dialog.Hide)

    val languages: SnapshotStateList<Language> = mutableStateListOf()
    val lines: SnapshotStateList<Line> = mutableStateListOf()

    val needShowMenu: MutableState<Boolean> = mutableStateOf(false)


    fun applyLanguagesAndLines(languages: List<Language>, lines: List<Line>) {
        this.languages.clear()
        this.languages.addAll(languages)
        this.lines.clear()
        this.lines.addAll(lines)
    }

    fun showLanguageDialog(hasMainLanguage: Boolean) {
        dialogState.value = Dialog.EnterLanguage(hasMainLanguage = hasMainLanguage)
    }

    fun hideDialog() {
        dialogState.value = Dialog.Hide
    }

    fun showSelectTranslateLanguageDialog(languages: List<Language>) {
        dialogState.value = Dialog.SelectUploadTranslateLanguage(languages)
    }

    fun showSelectUnloadXmlLanguage(languages: List<Language>) {
        dialogState.value = Dialog.SelectUnloadXmlLanguage(languages)
    }

}

sealed class MainScreenEffects {

}

sealed class MainScreenAction {
    data object Initialize : MainScreenAction()
    data class UploadLanguageFile(val file: PlatformFile): MainScreenAction()
    data class SaveLines(val language: Language) : MainScreenAction()
    data object RequestUnloadXml : MainScreenAction()
    data class UnloadXml(val language: Language) : MainScreenAction()
    data class UploadTranslateFile(val file: PlatformFile) : MainScreenAction()
    data class AddTranslatedLines(val language: Language) : MainScreenAction()
    data object HideDialog : MainScreenAction()

}