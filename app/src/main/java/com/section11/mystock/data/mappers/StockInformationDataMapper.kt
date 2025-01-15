package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.GraphNodeResponse
import com.section11.mystock.data.dto.PriceMovementResponse
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.SummaryResponse
import com.section11.mystock.domain.models.GraphInformation
import com.section11.mystock.domain.models.GraphNode
import com.section11.mystock.domain.models.GraphNodeDate.Companion.GRAPH_DEFAULT_HORIZONTAL_LABELS
import com.section11.mystock.domain.models.GraphNodeDate.Companion.GRAPH_NODE_DATE_FORMAT
import com.section11.mystock.domain.models.GraphNodeDate.Companion.SERVICE_RESPONSE_DATE_FORMAT
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.Summary
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val ONE = 1

fun StockInformationResponse.toStockInformation(): StockInformation {
    return StockInformation(
        summary = summary.toSummary(),
        graph = graph.toGraphInformation()
    )
}

private fun SummaryResponse.toSummary(): Summary {
    return Summary(
        title = title,
        stock = stock,
        exchange = exchange,
        price = price,
        currency = currency,
        priceMovement = priceMovement.toPriceMovement()
    )
}

private fun PriceMovementResponse.toPriceMovement(): PriceMovement {
    return PriceMovement(
        percentage = percentage,
        value = value,
        movement = movement
    )
}

private fun GraphNodeResponse.toGraphNode(): GraphNode {
    val inputFormatter = DateTimeFormatter.ofPattern(SERVICE_RESPONSE_DATE_FORMAT, Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern(GRAPH_NODE_DATE_FORMAT)

    val zonedDateTime = ZonedDateTime.parse(this.date, inputFormatter)
    val localDateTime = zonedDateTime.withZoneSameInstant(systemDefault())

    return GraphNode(
        price = price,
        dateLabel = localDateTime.format(outputFormatter)
    )
}

private fun List<GraphNodeResponse>.toGraphInformation(): GraphInformation {
    val graphNodes = this.map { it.toGraphNode() }
    return GraphInformation(
        graphNodes = graphNodes,
        horizontalAxisLabels = graphNodes.getHorizontalAxisLabels()
    )
}

/**
 * Gets the horizontal axis labels for the graph. Meaning the text that you'll see on the bottom of the
 * graph aligned with the label to be drawn at the bottom.
 *
 * We get the indexStepSize which means how much we need to go up the index under each line.
 * So for ex. if we had 100 values. And we want to show labels under 4 lines that cross the graph.
 * Then well be dividing the values in 5 (4 lines divide the graph in 5 blocks). So the total values
 * divided by five would be 20.
 * So under the first grid line, we want to show value at index 20,
 * in the second grid line we want to show value at index 40
 * and so on...
 *
 * @return a [List] of [String] with labels for the bottom fo the graph
 */
private fun List<GraphNode>.getHorizontalAxisLabels(): List<String> {
    val horizontalAxisLabels = mutableListOf<String>()
    val indexStepSize = size / (GRAPH_DEFAULT_HORIZONTAL_LABELS + ONE)

    for (i in 1..GRAPH_DEFAULT_HORIZONTAL_LABELS) {
        horizontalAxisLabels.add(this[indexStepSize * i].dateLabel)
    }
    return horizontalAxisLabels
}
