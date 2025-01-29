package com.section11.mystock.framework.secretmenu.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.section11.mystock.framework.environment.EnvironmentManager.Environment
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Prod
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Test
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel.CurrentSelectedEnvironment
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel.SecretMenuEvent.ChangeEnvironment
import com.section11.mystock.framework.utils.DarkAndLightPreviews
import com.section11.mystock.ui.theme.LocalDimens
import com.section11.mystock.ui.theme.LocalSpacing
import com.section11.mystock.ui.theme.MyStockTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TITLE = "Secret Menu"
private const val SELECT_ENVIRONMENT_TEXT = "Select Environment"
private const val APPLY_BUTTON_TEXT = "Apply Change"

@Composable
fun SecretMenuScreen(
    secretMenuStateFlow: StateFlow<CurrentSelectedEnvironment>,
    onSecretMenuEvent: (SecretMenuViewModel.SecretMenuEvent) -> Unit
) {
    val spacing = LocalSpacing.current
    val dimens = LocalDimens.current

    val secretMenuState by secretMenuStateFlow.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var currentEnvironment by remember { mutableStateOf(secretMenuState.environment) }
        Text(text = TITLE, fontSize = dimens.textExtraLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(spacing.large))

        SecretMenuDropDown(currentEnvironment) { newEnvironment ->
            currentEnvironment = newEnvironment
        }

        if (currentEnvironment != secretMenuState.environment) {
            Button(
                onClick = { onSecretMenuEvent(ChangeEnvironment(currentEnvironment)) },
                modifier = Modifier
                    .padding(top = spacing.medium)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = APPLY_BUTTON_TEXT)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretMenuDropDown(environment: Environment, onEnvironmentChange: (Environment) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = environment.name,
            onValueChange = {},
            label = { Text(SELECT_ENVIRONMENT_TEXT) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(Prod.name) },
                onClick = {
                    expanded = false
                    onEnvironmentChange(Prod)
                }
            )
            DropdownMenuItem(
                text = { Text(Test.name) },
                onClick = {
                    expanded = false
                    onEnvironmentChange(Test)
                }
            )
        }
    }
}

@DarkAndLightPreviews
@Composable
fun SecretMenuScreenPreview() {
    val secretMenuStateFlow = CurrentSelectedEnvironment(Prod)
    MyStockTheme {
        Surface {
            SecretMenuScreen(
                secretMenuStateFlow = MutableStateFlow(secretMenuStateFlow),
                onSecretMenuEvent = {}
            )
        }
    }
}
