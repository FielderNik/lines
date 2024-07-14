package com.nikulin.lines.presentation.upload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikulin.lines.domain.models.Language
import com.nikulin.lines.presentation.localcomposition.LocalNavigator
import com.nikulin.lines.presentation.navigation.AppNavigator
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import kotlinx.coroutines.flow.SharedFlow

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

@OptIn(ExperimentalMaterial3Api::class)
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
        BasicAlertDialog(
            modifier = Modifier.padding(24.dp),
            onDismissRequest = {
                showLanguageDialog = false
            }
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    Text("Введите язык")
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = screenState.languageValue.value.orEmpty(),
                        onValueChange = {
                            screenState.languageValue.value = it
                        }

                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            screenState.languageValue.value?.also {
                                onAction(
                                    UploadScreenAction.SaveLines(language = Language(it))
                                )
                                showLanguageDialog = false
                            }
                        }
                    ) {
                        Text("Принять")
                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = {
                    launcher.launch()
                }
            ) {
                Text("Загрузить")
            }
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