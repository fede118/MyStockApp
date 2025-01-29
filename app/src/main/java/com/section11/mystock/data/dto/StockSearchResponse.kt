package com.section11.mystock.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse

@Keep
data class StockSearchResponse(
    val summary: SummaryResponse?,
    @SerializedName("futures_chain") val closeMatchStock: List<CloseMatchStockResponse>?
)

@Keep
data class CloseMatchStockResponse(
    val stock: String,
    @SerializedName("date") val title: String,
    @SerializedName("extracted_price") val extractedPrice: String,
    @SerializedName("price_movement") val priceMovement: CloseMatchPriceMovementResponse,
    val currency: String
)

@Keep
data class CloseMatchPriceMovementResponse(
    val percentage: Double,
    val movement: String
)
