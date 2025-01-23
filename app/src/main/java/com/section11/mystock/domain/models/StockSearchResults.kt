package com.section11.mystock.domain.models

data class StockSearchResults(
    val exactMatch: Summary?,
    val closeMatchStocks: List<CloseMatchStock>?
)

data class CloseMatchStock(
    val stock: String,
    val title: String,
    val extractedPrice: String,
    val currency: String,
    val priceMovement: CloseMatchStockPriceMovement
)

data class CloseMatchStockPriceMovement(
    val percentage: Double,
    val movement: String
)
