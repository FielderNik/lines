package com.nikulin.lines.presentation.upload

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.nikulin.lines.domain.models.Language
import io.github.vinceglb.filekit.core.PlatformFile

class UploadScreenState {

    val languageValue: MutableState<String?> = mutableStateOf(null)

}

sealed class UploadScreenEffect {
    data object OpenMainScreen : UploadScreenEffect()
}

sealed class UploadScreenAction {
    data object RequestUploadFile : UploadScreenAction()
    data class ApplyFile(val file: PlatformFile): UploadScreenAction()
    data class SaveLines(val language: Language) : UploadScreenAction()
}