package com.nikulin.lines.presentation.upload

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.presentation.components.dialogs.LanguageDialog
import com.nikulin.lines.presentation.localcomposition.LocalNavigator
import com.nikulin.lines.presentation.navigation.AppNavigator
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import kotlinx.coroutines.flow.SharedFlow
import lines.composeapp.generated.resources.Res
import lines.composeapp.generated.resources.action_upload
import org.jetbrains.compose.resources.stringResource

@Composable
fun UploadRoute(
    screenState: UploadScreenState,
    effects: SharedFlow<UploadScreenEffect>,
    sendAction: (UploadScreenAction) -> Unit
) {

    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        effects.collect { effect ->
            handleUploadEffect(effect, navigator)
        }
    }

    UploadScreen(screenState, sendAction)

}

@Composable
private fun UploadScreen(
    screenState: UploadScreenState,
    onAction: (UploadScreenAction) -> Unit,
) {
    var showLanguageDialog by remember { mutableStateOf(false) }

    val launcher = rememberFilePickerLauncher() { file ->
        file?.also { platformFile ->
            onAction(
                UploadScreenAction.ApplyFile(platformFile)
            )
            showLanguageDialog = true
        }
    }

    if (showLanguageDialog) {
        LanguageDialog(
            languageState = screenState.languageValue,
            isMainLanguageState = screenState.isMainLanguage,
            hasMainLanguage = screenState.hasMainLanguage.value,
            onApply = { language ->
                onAction(
                    UploadScreenAction.SaveLines(language)
                )
            },
            onClose = {
                showLanguageDialog = false
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Button(
            onClick = launcher::launch
        ) {
            Text(stringResource(Res.string.action_upload))
        }

    }
}

private suspend fun handleUploadEffect(
    effect: UploadScreenEffect,
    navigator: AppNavigator,
) {
    when (effect) {
        UploadScreenEffect.OpenMainScreen -> {
            navigator.openMain()
        }
    }
}