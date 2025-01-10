package com.section11.mystock.domain.models

data class StockInformation(
    val summary: Summary
)

data class Summary(
    val title: String,
    val stock: String,
    val exchange: String,
    val price: String,
    val currency: String,
    val priceMovement: PriceMovement
)

data class PriceMovement(
    val percentage: Double,
    val value: Double,
    val movement: String
)
