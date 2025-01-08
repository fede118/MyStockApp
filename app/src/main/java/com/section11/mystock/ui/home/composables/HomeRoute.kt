package com.section11.mystock.ui.home.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.section11.mystock.models.Stock
import com.section11.mystock.ui.home.HomeViewModel

@Composable
fun HomeRoute(homeViewModel: HomeViewModel, navigateToSingleStock: (Stock) -> Unit) {
    val uiState by homeViewModel.uiState.collectAsState()
    homeViewModel.getStocks()

    HomeScreenStockList(
        uiState = uiState,
        onStockTap = { stock -> navigateToSingleStock(stock) }
    )
}
