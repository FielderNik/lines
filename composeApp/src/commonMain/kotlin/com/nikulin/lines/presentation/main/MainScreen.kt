package com.nikulin.lines.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nikulin.lines.presentation.components.dialogs.LanguageDialog
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import kotlinx.coroutines.flow.SharedFlow
import lines.composeapp.generated.resources.Res
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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

    val column1Weight = .3f // 30%
    val column2Weight = .7f // 70%


    val launcher = rememberFilePickerLauncher() { file ->
        file?.also { platformFile ->
            sendAction(
                MainScreenAction.ApplyFile(platformFile)
            )
            screenState.showLanguageDialog.value = true
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                DropdownMenu(
                    expanded = screenState.showMenu.value,
                    onDismissRequest = {
                        screenState.showMenu.value = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text("Добавить язык")
                        },
                        onClick = {
                            screenState.showMenu.value = false
                            launcher.launch()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Выгрузить для перевода")
                        },
                        onClick = {

                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text("Загрузить перевод")
                        },
                        onClick = {

                        }
                    )
                }
                FloatingActionButton(
                    onClick = {
                        screenState.showMenu.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "upload"
                    )
                }
            }
        }
    ) {

        if (screenState.showLanguageDialog.value) {
            LanguageDialog(
                languageState = screenState.languageValue,
                isMainLanguageState = screenState.isMainLanguage,
                hasMainLanguage = screenState.hasMainLanguage.value,
                onApply = { language ->
                    sendAction(
                        MainScreenAction.SaveLines(language)
                    )
                },
                onClose = {
                    screenState.showLanguageDialog.value = false
                }
            )
        }

        LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {

            stickyHeader {
                Row(Modifier.background(Color.Gray)) {

                    TableCellRightBorder(text = stringResource(Res.string.title_key), weight = column1Weight)

                    screenState.languages.forEachIndexed { index, language ->
                        val languageText = if (!language.isMain) {
                            language.value
                        } else {
                            "${language.value} ${stringResource(Res.string.title_main_with_brackets)}"
                        }

                        if (index != screenState.languages.lastIndex) {
                            TableCellRightBorder(text = languageText, weight = ((1f - column1Weight) / screenState.languages.size))
                        } else {

                            TableCell(text = languageText, weight = ((1f - column1Weight) / screenState.languages.size))
                        }

                    }
                }
                HorizontalDivider()
            }

            itemsIndexed(items = screenState.lines) { index, item ->
                val (key, translations) = item
                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCellRightBorder(text = key, weight = column1Weight)

                    screenState.languages.forEachIndexed { index, language ->
                        val tr = translations[language]
                        if (index != screenState.languages.lastIndex) {
                            TableCellRightBorder(text = tr?.value.orEmpty(), weight = ((1f - column1Weight) / screenState.languages.size))
                        } else {
                            TableCell(text = tr?.value.orEmpty(), weight = ((1f - column1Weight) / screenState.languages.size))
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
fun RowScope.TableCell(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .weight(weight)
            .background(color = if (text.isNotEmpty()) Color.Transparent else Color.Red)
            .padding(8.dp)
    )
}

@Composable
fun RowScope.TableCellRightBorder(
    text: String,
    weight: Float,
) {
    Text(
        text = text,
        Modifier
            .rightBorder(1.dp, Color.Black)
            .weight(weight)
            .background(color = if (text.isNotEmpty()) Color.Transparent else Color.Red)
            .padding(8.dp)

    )
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

fun Modifier.rightBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current
        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx/2

            drawLine(
                color = color,
                start = Offset(x = width, y = 0f),
                end = Offset(x = width , y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)
