package com.section11.mystock.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Error
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Success
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.home.composables.StockList

@Composable
fun HomeRoute(homeViewModel: HomeViewModel, navigateToSingleStock: (String) -> Unit) {
    val uiState by homeViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getStocks()
    }

    // Collect navigation events
    LaunchedEffect(homeViewModel.navigationEvent) {
        homeViewModel.navigationEvent.collect { event ->
            when (event) {
                is ToSingleStock -> navigateToSingleStock(event.symbol)
            }
        }
    }

    when (uiState) {
        is Loading -> FullScreenLoading()
        is Success -> StockList(
            stocks = (uiState as Success).stocks,
            onStockTap = { stock ->
                homeViewModel.onStockTap(stock.symbol)
            },
            singleStockInfoState = homeViewModel.singleStockInformationState
        )
        is Error -> Text("Error: ${(uiState as Error).message}")
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}
