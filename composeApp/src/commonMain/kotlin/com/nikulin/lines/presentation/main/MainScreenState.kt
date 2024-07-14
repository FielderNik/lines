package com.nikulin.lines.presentation.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.domain.models.Line

class MainScreenState {
    val languages: SnapshotStateList<Language> = mutableStateListOf()
    val lines: SnapshotStateList<Line> = mutableStateListOf()

    fun applyLanguagesAndLines(languages: List<Language>, lines: List<Line>) {
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
}