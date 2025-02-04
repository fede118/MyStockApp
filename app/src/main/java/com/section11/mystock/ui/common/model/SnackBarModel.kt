package com.section11.mystock.ui.common.model

import androidx.compose.material3.SnackbarDuration

data class SnackBarModel(
    val message: String,
    val actionLabel: String? = null,
    val link: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short
)
