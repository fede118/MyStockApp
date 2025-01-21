package com.section11.mystock.ui.model

import androidx.compose.ui.graphics.Color

data class WatchlistScreenUiModel(
    val searchHint: String,
    val stocks: List<WatchlistStockModel>
)

data class WatchlistStockModel(
    val stockTitle: String,
    val symbol: String,
    val percentageChange: String,
    val percentageColor: Color
)
