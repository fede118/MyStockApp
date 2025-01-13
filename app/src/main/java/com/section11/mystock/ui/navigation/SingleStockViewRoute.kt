package com.section11.mystock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import com.section11.mystock.ui.singlestock.composables.SingleStockScreen

@Composable
fun SingleStockViewRoute(stockSymbol: String?, singleStockViewModel: SingleStockViewModel) {
    val uiState by singleStockViewModel.uiState.collectAsState()
    LaunchedEffect(key1 = stockSymbol) {
        singleStockViewModel.getStockInformation(stockSymbol)
    }

    (uiState as? SingleStockViewModel.SingleStockUiState.Success)?.let { successState ->
        SingleStockScreen(successState)
    }
}
