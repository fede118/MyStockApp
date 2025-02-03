package com.section11.mystock.ui.common.extentions

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.section11.mystock.ui.common.model.SnackBarModel
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CoroutineScopeExtensionsKtTest {

    private val coroutineScope = mockk<CoroutineScope>()
    private val snackbarHostState: SnackbarHostState = mock()
    private val context: Context = mock()

    @Test
    fun `test ShowSnackBar with null link should only show message`() = runTest {
        val message = "message"
        val snackBarModel: SnackBarModel = mock()
        whenever(snackBarModel.message).thenReturn(message)
        whenever(snackBarModel.link).thenReturn(null)
        whenever(snackbarHostState.showSnackbar(anyString(), any(), any(), any())).thenReturn(SnackbarResult.Dismissed)

        coroutineScope.showSnackBar(snackBarModel, snackbarHostState, context)

        verify(snackbarHostState).showSnackbar(message)
    }

    @Test
    fun `test ShowSnackBar with link should show snackbar with action`() = runTest {
        val message = "message"
        val snackBarModel: SnackBarModel = mock()
        whenever(snackBarModel.message).thenReturn(message)
        whenever(snackBarModel.link).thenReturn("some link")
        whenever(snackBarModel.duration).thenReturn(SnackbarDuration.Short)
        whenever(snackbarHostState.showSnackbar(anyString(), any(), any(), any()))
            .thenReturn(SnackbarResult.ActionPerformed)
        doNothing().whenever(context).startActivity(any())

        coroutineScope.showSnackBar(snackBarModel, snackbarHostState, context)

        verify(snackbarHostState).showSnackbar(message)
    }
}
