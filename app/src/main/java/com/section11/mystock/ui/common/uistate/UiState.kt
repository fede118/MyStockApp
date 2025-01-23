package com.section11.mystock.ui.common.uistate

interface UiState {
    data object Loading : UiState
    data object Idle : UiState
    data class Error(val message: String?) : UiState
}
