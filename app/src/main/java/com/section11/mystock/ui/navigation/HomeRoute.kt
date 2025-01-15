package com.section11.mystock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.composables.HomeScreenStockList

@Composable
fun HomeRoute(homeViewModel: HomeViewModel, navigateToSingleStock: (String) -> Unit) {
    val uiState by homeViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        homeViewModel.getStocks()
    }

    HomeScreenStockList(
        uiState = uiState,
        onStockTap = { stock -> navigateToSingleStock(stock.symbol) }
    )
}
