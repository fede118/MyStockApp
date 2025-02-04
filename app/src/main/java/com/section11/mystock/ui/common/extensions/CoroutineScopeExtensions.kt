package com.section11.mystock.ui.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.section11.mystock.ui.common.model.SnackBarModel
import kotlinx.coroutines.CoroutineScope

suspend fun CoroutineScope.showSnackBar(
    snackBarModel: SnackBarModel,
    snackbarHostState: SnackbarHostState,
    context: Context
) {
    if (snackBarModel.link == null) {
        snackbarHostState.showSnackbar(message = snackBarModel.message)
    } else {
        val result = snackbarHostState.showSnackbar(
            message = snackBarModel.message,
            actionLabel = snackBarModel.actionLabel,
            duration = snackBarModel.duration
        )

        if (result == SnackbarResult.ActionPerformed) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(snackBarModel.link))
            context.startActivity(intent)
        }
    }
}
