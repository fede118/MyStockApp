package com.section11.mystock.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StockInformationResponse(
    val summary: SummaryResponse,
    val graph: List<GraphNodeResponse>
)

@Keep
data class SummaryResponse(
    val title: String,
    val stock: String,
    val exchange: String,
    val currency: String,
    @SerializedName("extracted_price") val price: String,
    @SerializedName("price_movement") val priceMovement: PriceMovementResponse
)

@Keep
data class PriceMovementResponse(
    val percentage: Double,
    val value: Double,
    val movement: String
)

@Keep
data class GraphNodeResponse(
    val price: Double,
    val date: String
)
