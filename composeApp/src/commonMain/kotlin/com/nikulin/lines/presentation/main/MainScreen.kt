package com.nikulin.lines.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nikulin.lines.presentation.components.dialogs.LanguageDialog
import com.nikulin.lines.presentation.components.dialogs.SelectLanguageDialog
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import kotlinx.coroutines.flow.SharedFlow
import lines.composeapp.generated.resources.Res
import lines.composeapp.generated.resources.action_add_language
import lines.composeapp.generated.resources.action_unload_xml
import lines.composeapp.generated.resources.action_upload_translate
import lines.composeapp.generated.resources.title_key
import lines.composeapp.generated.resources.title_main_with_brackets
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainRoute(
    screenState: MainScreenState,
    effects: SharedFlow<MainScreenEffects>,
    sendAction: (MainScreenAction) -> Unit
) {

    LaunchedEffect(Unit) {
        sendAction(MainScreenAction.Initialize)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        MainScreen(
            screenState = screenState,
            sendAction = sendAction
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    screenState: MainScreenState,
    sendAction: (MainScreenAction) -> Unit,
) {

    Dialogs(dialogState = screenState.dialogState, sendAction = sendAction)

    val column1Weight = .3f // 30%

    val languagePicker = rememberFilePickerLauncher { file ->
        file?.also { platformFile ->
            sendAction(
                MainScreenAction.UploadLanguageFile(platformFile)
            )
        }
    }

    val translatePicker = rememberFilePickerLauncher { file ->
        file?.also { platformFile ->
            sendAction(
                MainScreenAction.UploadTranslateFile(platformFile)
            )
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                MainMenu(
                    showMenuState = screenState.needShowMenu,
                    onAddClicked = languagePicker::launch,
                    onUnloadXmlClicked = {
                        sendAction(MainScreenAction.RequestUnloadXml)
                    },
                    onUploadTranslateClicked = translatePicker::launch
                )
                FloatingActionButton(
                    onClick = {
                        screenState.needShowMenu.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "upload"
                    )
                }
            }
        }
    ) {

        val columnWeight by remember(screenState.lines) {
            derivedStateOf {
                (1f - column1Weight) / screenState.languages.size
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(0.3f))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            stickyHeader {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondary)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TableCell(
                        text = stringResource(Res.string.title_key),
                        textColor = Color.White,
                        weight = column1Weight
                    )
                    VerticalDivider(modifier = Modifier.fillMaxHeight())

                    screenState.languages.forEachIndexed { index, language ->
                        val languageText = if (!language.isMain) {
                            language.value
                        } else {
                            "${language.value} ${stringResource(Res.string.title_main_with_brackets)}"
                        }

                        TableCell(
                            textColor = Color.White,
                            text = languageText,
                            weight = columnWeight
                        )

                        if (index != screenState.languages.lastIndex) {
                            VerticalDivider(modifier = Modifier.fillMaxHeight())
                        }
                    }
                }
                HorizontalDivider()
            }

            itemsIndexed(items = screenState.lines) { index, item ->
                val (key, translations) = item
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TableCell(text = key, weight = column1Weight)

                    VerticalDivider(modifier = Modifier.fillMaxHeight())

                    screenState.languages.forEachIndexed { index, language ->
                        val translation = translations[language]

                        TableCell(text = translation?.value.orEmpty(), weight = columnWeight)

                        if (index != screenState.languages.lastIndex) {
                            VerticalDivider(modifier = Modifier.fillMaxHeight())
                        }

                    }
                }

                if (index != screenState.lines.lastIndex) {
                    HorizontalDivider()
                }

            }
        }
    }

}


@Composable
private fun Dialogs(
    dialogState: State<MainScreenState.Dialog>,
    sendAction: (MainScreenAction) -> Unit,
) {
    when (val state = dialogState.value) {
        is MainScreenState.Dialog.EnterLanguage -> {
            LanguageDialog(
                languageState = state.languageState,
                isMainLanguageState = state.isMainLanguage,
                hasMainLanguage = state.hasMainLanguage,
                onApply = { language ->
                    sendAction(MainScreenAction.SaveLines(language))
                },
                onClose = {
                    sendAction(MainScreenAction.HideDialog)
                }
            )
        }

        MainScreenState.Dialog.Hide -> Unit

        is MainScreenState.Dialog.SelectUnloadXmlLanguage -> {
            SelectLanguageDialog(
                languages = state.languages,
                selectedLanguage = state.selectedLanguage,
                onApply = { language ->
                    sendAction(MainScreenAction.UnloadXml(language))
                },
                onClose = {
                    sendAction(MainScreenAction.HideDialog)
                }
            )
        }

        is MainScreenState.Dialog.SelectUploadTranslateLanguage -> {
            SelectLanguageDialog(
                languages = state.languages,
                selectedLanguage = state.selectedLanguage,
                onApply = { language ->
                    sendAction(MainScreenAction.AddTranslatedLines(language))
                },
                onClose = {
                    sendAction(MainScreenAction.HideDialog)
                }
            )
        }
    }
}

@Composable
private fun MainMenu(
    showMenuState: MutableState<Boolean>,
    onAddClicked: () -> Unit,
    onUnloadXmlClicked: () -> Unit,
    onUploadTranslateClicked: () -> Unit,
) {
    DropdownMenu(
        expanded = showMenuState.value,
        onDismissRequest = {
            showMenuState.value = false
        }
    ) {
        DropdownMenuItem(
            text = {
                Text(stringResource(Res.string.action_add_language))
            },
            onClick = {
                showMenuState.value = false
                onAddClicked()
            }
        )
        DropdownMenuItem(
            text = {
                Text(stringResource(Res.string.action_upload_translate))
            },
            onClick = {
                showMenuState.value = false
                onUploadTranslateClicked()
            }
        )
        DropdownMenuItem(
            text = {
                Text(stringResource(Res.string.action_unload_xml))
            },
            onClick = {
                showMenuState.value = false
                onUnloadXmlClicked()
            }
        )
    }
}

@Composable
private fun RowScope.TableCell(
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    weight: Float,
) {
    Text(
        text = text,
        color = textColor,
        modifier = Modifier
            .weight(weight)
            .background(color = if (text.isNotEmpty()) Color.Transparent else MaterialTheme.colorScheme.errorContainer)
            .padding(8.dp)
    )
}