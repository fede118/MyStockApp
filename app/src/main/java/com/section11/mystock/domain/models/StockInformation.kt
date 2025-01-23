package com.section11.mystock.domain.models

import java.util.Date

data class StockInformation(
    val summary: Summary,
    val graph: GraphInformation
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

/**
 * Represents the graph information.
 *
 * @property graphNodes are simply just the nodes on the graph
 * @property edgeLabels are the labels to be shown on the edges of the graph at X. So at the
 * start and the end of the graph, meaning the starting and finish points.
 * @property horizontalAxisLabels are the labels to be shown at the bottom of the graph, under each
 * inner line of the background grid. Because the edges are already taken care of by [graphEdgeLabels]
 */
data class GraphInformation(
    val graphNodes: List<GraphNode>,
    val edgeLabels: Pair<String, String>,
    val horizontalAxisLabels: List<String>
)

data class GraphNode(
    val price: Double,
    val dateLabel: String
)

class GraphNodeDate : Date() {
    companion object {
        const val SERVICE_RESPONSE_DATE_FORMAT = "MMM dd yyyy, hh:mm a 'UTC'XXX"
        const val GRAPH_NODE_DATE_FORMAT = "HH:mm"
        const val GRAPH_DEFAULT_HORIZONTAL_LABELS = 4
    }
}
