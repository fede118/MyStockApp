package com.section11.mystock.ui.model

import androidx.compose.ui.graphics.Color

data class WatchlistScreenUiModel(
    val searchHint: String,
    val stocks: List<WatchlistStockModel>,
    val appVersionInfo: String
)

data class WatchlistStockModel(
    val stockTitle: String,
    val symbol: String,
    val exchange: String,
    val percentageChange: String,
    val percentageColor: Color
)
