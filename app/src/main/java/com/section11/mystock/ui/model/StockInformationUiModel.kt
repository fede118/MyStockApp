package com.section11.mystock.ui.model

import androidx.compose.ui.graphics.Color
import com.section11.mystock.ui.common.model.SnackBarModel

data class StockInformationUiModel(
    val summaryUiModel: SummaryUiModel,
    val graphModel: GraphUiModel,
    val knowledgeGraph: KnowledgeGraphUiModel
) {
    data class SummaryUiModel(
        val title: String,
        val stockSymbolLabel: String,
        val exchangeLabel: String,
        val priceLabel: String,
        val priceMovementTitle: String,
        val priceMovementValueLabel: String,
        val priceMovementPercentage: String,
        val priceMovementColor: Color,
    )

    /**
     * Ui model of the graph to be drawn. It contains all the UI information for the graph
     *
     * @property graphPoints the [List] of [Double] representing the data to be drawn in the graph
     * @property graphEdgeLabels nullable [Pair] of [String] representing the labels to be drawn at the
     * start and end of the graph (meaning the first and last values on the X axis)
     * @property graphHorizontalLabels nullable [List] of [String] representing the labels to be drawn
     * at the bottom of the graph under each vertical line
     * @property graphGridVerticalLinesAmount the amount of background lines that the graph is
     * divided into vertically
     * @property graphGridHorizontalLinesAmount the amount of background lines that the graph is
     * divided into horizontally
     */
    data class GraphUiModel(
        val graphPoints: List<Double>,
        val graphEdgeLabels: Pair<String, String>? = null,
        val graphHorizontalLabels: List<String>? = null,
        val graphGridVerticalLinesAmount: Int? = null,
        val graphGridHorizontalLinesAmount: Int? = null
    )

    data class KnowledgeGraphUiModel(
        val keyStats: KeyStatsUiModel,
        val about: List<AboutUiModel>
    ) {
        data class KeyStatsUiModel(
            val tags: List<TagUiModel>,
            val stats: List<StatUiModel>,
            val climateChange: ClimateChangeScoreUiModel
        ) {
            data class TagUiModel(
                val text: String,
                val description: String,
                val tagColor: Color,
                val link: String? = null,
                val onTapSnackBarModel: SnackBarModel? = null
            )

            data class StatUiModel(
                val label: String,
                val description: String,
                val value: String
            )

            data class ClimateChangeScoreUiModel(
                val title: String,
                val score: String,
                val link: String
            )
        }

        data class AboutUiModel(
            val title: String,
            val description: DescriptionUiModel,
            val info: List<InfoUiModel>
        ) {
            data class DescriptionUiModel(
                val snippet: String,
                val link: String,
                val linkText: String
            )

            data class InfoUiModel(
                val label: String,
                val value: String,
                val link: String? = null
            )
        }
    }
}
