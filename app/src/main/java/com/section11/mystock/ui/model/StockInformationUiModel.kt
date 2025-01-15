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
    val priceMovementColor: Color,
    val graphModel: GraphUiModel
)

/**
 * Ui model of the graph to be drawn. It contains all the UI information for the graph
 *
 * @property graphPoints the [List] of [Double] representing the data to be drawn in the graph
 * @property graphHorizontalLabels nullable [List] of [String] representing the labels to be drawn
 * at the bottom of the graph under each vertical line
 * @property graphBackgroundVerticalLinesAmount the amount of background lines that the graph is
 * divided into vertically
 * @property graphBackgroundHorizontalLinesAmount the amount of background lines that the graph is
 * divided into horizontally
 */
data class GraphUiModel(
    val graphPoints: List<Double>,
    val graphHorizontalLabels: List<String>? = null,
    val graphBackgroundVerticalLinesAmount: Int? = null,
    val graphBackgroundHorizontalLinesAmount: Int? = null
)
