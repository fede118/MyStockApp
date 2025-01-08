package com.section11.mystock.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStocksNavGraph
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun MyStocksApp() {
    MyStockTheme {
        val navController = rememberNavController()
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            MyStocksNavGraph(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                navController = navController,
                startDestination = HOME_ROUTE /* currentRoute */
            )
        }
    }
}

@Preview
@Composable
fun MyStocksAppPreview() {
    MyStocksApp()
}
