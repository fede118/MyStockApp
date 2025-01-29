package com.section11.mystock.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStocksNavGraph
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun MyStocksApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    var bottomBarContent by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }

    MyStockTheme(snackbarHostState = snackbarHostState) {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = { bottomBarContent?.invoke() },
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            MyStocksNavGraph(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                onBottomBarChange = { bottomBarContent = it },
                navController = navController,
                startDestination = HOME_ROUTE
            )
        }
    }
}

@Preview
@Composable
fun MyStocksAppPreview() {
    MyStocksApp()
}
