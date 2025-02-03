package com.section11.mystock.data.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StockInformationResponse(
    val summary: SummaryResponse,
    val graph: List<GraphNodeResponse>,
    @SerializedName("knowledge_graph")
    val knowledgeGraph: KnowledgeGraphResponse
) {
    @Keep
    data class SummaryResponse(
        val title: String,
        @SerializedName("stock")
        val symbol: String,
        val exchange: String,
        val currency: String,
        @SerializedName("extracted_price")
        val price: String,
        @SerializedName("price_movement")
        val priceMovement: PriceMovementResponse
    ) {
        @Keep
        data class PriceMovementResponse(
            val percentage: Double,
            val value: Double,
            val movement: String
        )
    }

    @Keep
    data class GraphNodeResponse(
        val price: Double,
        val date: String
    )

    @Keep
    data class KnowledgeGraphResponse(
        @SerializedName("key_stats")
        val keyStats: KeyStatsResponse,
        val about: List<AboutResponse>
    ) {
        @Keep
        data class KeyStatsResponse(
            val tags: List<TagResponse>,
            val stats: List<StatResponse>,
            @SerializedName("climate_change")
            val climateChange: ClimateChangeResponse?
        ) {
            @Keep
            data class TagResponse(
                val text: String,
                val description: String,
                val link: String? = null
            )

            @Keep
            data class StatResponse(
                val label: String,
                val description: String,
                val value: String
            )

            @Keep
            data class ClimateChangeResponse(
                val score: String,
                val link: String
            )
        }

        @Keep
        data class AboutResponse(
            val title: String,
            val description: DescriptionResponse,
            val info: List<InfoResponse>
        ) {
            @Keep
            data class DescriptionResponse(
                val snippet: String,
                val link: String,
                @SerializedName("link_text")
                val linkText: String
            )

            @Keep
            data class InfoResponse(
                val label: String,
                val value: String,
                val link: String? = null
            )
        }
    }
}
