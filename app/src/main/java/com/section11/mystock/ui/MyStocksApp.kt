package com.section11.mystock.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.section11.mystock.ui.home.HomeScreenStockList
import com.section11.mystock.ui.theme.MyStockTheme

@Composable
fun MyStocksApp() {
    MyStockTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            HomeScreenStockList(Modifier.padding(bottom = paddingValues.calculateBottomPadding()))
        }
    }
}

@Preview
@Composable
fun MyStocksAppPreview() {
    MyStocksApp()
}
