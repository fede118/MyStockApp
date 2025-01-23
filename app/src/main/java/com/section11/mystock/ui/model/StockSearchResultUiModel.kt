package com.section11.mystock.ui.model

import androidx.compose.ui.graphics.Color

data class StockSearchResultUiModel(
    val title: String,
    val symbol: String,
    val symbolBoxColor: Color,
    val priceLabel: String,
    val priceMovementSymbol: String,
    val priceMovementColor: Color,
    val percentage: String
)
