package com.section11.mystock.ui.model

import androidx.compose.ui.graphics.Color

data class StockInformationUiModel(
    val title: String,
    val stockSymbolLabel: String,
    val exchangeLabel: String,
    val priceLabel: String,
    val priceMovementTitle: String,
    val priceMovementValueLabel: String,
    val priceMovementPercentage: String,
    val priceMovementColor: Color
)
