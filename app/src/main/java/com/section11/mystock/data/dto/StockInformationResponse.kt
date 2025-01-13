package com.section11.mystock.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StockInformationResponse(
    val summary: SummaryResponse
)

@Keep
data class SummaryResponse(
    val title: String,
    val stock: String,
    val exchange: String,
    val price: String,
    val currency: String,
    @SerializedName("price_movement") val priceMovement: PriceMovementResponse
)

@Keep
data class PriceMovementResponse(
    val percentage: Double,
    val value: Double,
    val movement: String
)
