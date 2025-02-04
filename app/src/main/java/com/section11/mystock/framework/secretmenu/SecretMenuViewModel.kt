package com.section11.mystock.framework.secretmenu

import androidx.lifecycle.ViewModel
import com.section11.mystock.framework.environment.EnvironmentManager
import com.section11.mystock.framework.environment.EnvironmentManager.Environment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SecretMenuViewModel @Inject constructor(
    private val environmentManager: EnvironmentManager
): ViewModel() {

    private val _secretMenuState = MutableStateFlow(
        CurrentSelectedEnvironment(environmentManager.currentEnvironment)
    )
    val secretMenuState: StateFlow<CurrentSelectedEnvironment> = _secretMenuState

    fun onChangeEnvironmentTapped(environment: Environment) {
        environmentManager.setEnvironment(environment)
        _secretMenuState.value = CurrentSelectedEnvironment(environment)
    }

    data class CurrentSelectedEnvironment(val environment: Environment)

    sealed class SecretMenuEvent {
        data class ChangeEnvironment(val environment: Environment) : SecretMenuEvent()
    }
}
