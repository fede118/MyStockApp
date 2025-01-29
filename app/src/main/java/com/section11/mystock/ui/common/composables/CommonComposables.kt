
package com.section11.mystock.ui.common.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.section11.mystock.framework.secretmenu.ShakeDetector
import com.section11.mystock.ui.theme.LocalSpacing

@Composable
fun SmallBodyText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        modifier =  modifier
    )
}

@Composable
fun MyStockLoader(modifier: Modifier = Modifier, paddingVertical: Dp = LocalSpacing.current.medium) {
    Box(modifier = modifier.fillMaxWidth().padding(vertical = paddingVertical)) {
        CircularProgressIndicator(modifier = modifier.align(Alignment.Center))
    }
}

@Composable
fun SecretMenuShakeDetector(onShakeDetected: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val shakeDetector = ShakeDetector(context = context) {
            onShakeDetected()
        }

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> shakeDetector.start()
                Lifecycle.Event.ON_PAUSE -> shakeDetector.stop()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            shakeDetector.stop()
        }
    }
}
