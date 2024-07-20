package com.nikulin.lines.presentation.upload

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.nikulin.lines.domain.models.Language
import io.github.vinceglb.filekit.core.PlatformFile

class UploadScreenState {

    val languageValue: MutableState<String?> = mutableStateOf(null)
    val isMainLanguage: MutableState<Boolean> = mutableStateOf(false)
    val hasMainLanguage: MutableState<Boolean> = mutableStateOf(false)

}

sealed class UploadScreenEffect {
    data object OpenMainScreen : UploadScreenEffect()
}

sealed class UploadScreenAction {
    data object Initialize : UploadScreenAction()
    data class ApplyFile(val file: PlatformFile): UploadScreenAction()
    data class SaveLines(val language: Language) : UploadScreenAction()
}