package com.nikulin.lines.presentation.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikulin.lines.domain.models.Language
import lines.composeapp.generated.resources.Res
import lines.composeapp.generated.resources.action_apply
import lines.composeapp.generated.resources.title_enter_language
import lines.composeapp.generated.resources.title_main
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDialog(
    languageState: MutableState<String?>,
    isMainLanguageState: MutableState<Boolean>,
    hasMainLanguage: Boolean,
    onApply: (Language) -> Unit,
    onClose: () -> Unit
) {
    BasicAlertDialog(
        modifier = Modifier.padding(24.dp),
        onDismissRequest = onClose
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

                Text(text = stringResource(Res.string.title_enter_language))

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = languageState.value.orEmpty(),
                    onValueChange = {
                        languageState.value = it
                    }

                )
                if (!hasMainLanguage) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(Res.string.title_main))
                        Checkbox(
                            checked = isMainLanguageState.value,
                            onCheckedChange = {
                                isMainLanguageState.value = it
                            }
                        )
                    }
                }
                Button(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    onClick = {
                        languageState.value?.also { languageValue ->
                            onApply(Language(languageValue, isMainLanguageState.value))
                            onClose()
                        }
                    }
                ) {
                    Text(text = stringResource(Res.string.action_apply))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLanguageDialog(
    languages: List<Language>,
    selectedLanguage: MutableState<Language?>,
    onApply: (Language) -> Unit,
    onClose: () -> Unit
) {
    BasicAlertDialog(
        modifier = Modifier.padding(24.dp),
        onDismissRequest = onClose
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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Text(text = stringResource(Res.string.title_enter_language))

                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedLanguage.value = language
                            },
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLanguage.value == language,
                            onClick = {
                                selectedLanguage.value = language
                            }
                        )
                        Text(text = language.value)
                    }
                }


                Button(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    onClick = {
                        selectedLanguage.value?.also {
                            onApply(it)
                            onClose()
                        }
                    }
                ) {
                    Text(text = stringResource(Res.string.action_apply))
                }
            }
        }
    }
}