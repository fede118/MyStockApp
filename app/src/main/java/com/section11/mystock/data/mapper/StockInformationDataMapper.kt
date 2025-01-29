package com.section11.mystock.data.mapper

import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.StockInformationResponse.GraphNodeResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.AboutResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.ClimateChangeResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.StatResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.TagResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse.PriceMovementResponse
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation.Companion.GRAPH_DEFAULT_HORIZONTAL_LABELS
import com.section11.mystock.domain.models.StockInformation.GraphInformation.Companion.GRAPH_NODE_DATE_FORMAT
import com.section11.mystock.domain.models.StockInformation.GraphInformation.Companion.SERVICE_RESPONSE_DATE_FORMAT
import com.section11.mystock.domain.models.StockInformation.GraphInformation.GraphNode
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.About
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.ClimateChange
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.Tag
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.domain.models.StockInformation.Summary.PriceMovement
import java.time.ZoneId.systemDefault
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val ONE = 1

fun StockInformationResponse.toStockInformation(): StockInformation {
    return StockInformation(
        summary = summary.toSummary(),
        graph = graph.toGraphInformation(),
        knowledgeGraph = knowledgeGraph.toKnowledgeGraph()

    )
}

fun SummaryResponse.toSummary(): Summary {
    return Summary(
        title = title,
        stock = stock,
        exchange = exchange,
        price = price,
        currency = currency,
        priceMovement = priceMovement.toPriceMovement()
    )
}

fun PriceMovementResponse.toPriceMovement(): PriceMovement {
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
        edgeLabels = graphNodes.getEdgeLabels(),
        horizontalAxisLabels = graphNodes.getHorizontalAxisLabels()
    )
}

private fun List<GraphNode>.getEdgeLabels(): Pair<String, String> {
    return Pair(first().dateLabel, last().dateLabel)
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

private fun KnowledgeGraphResponse.toKnowledgeGraph(): KnowledgeGraph {
    return KnowledgeGraph(
        keyStats = keyStats.toKeyStats(),
        about = about.toAbout()
    )
}

private fun KeyStatsResponse.toKeyStats(): KeyStats {
    return KeyStats(
        tags = tags.toTags(),
        stats = stats.toStats(),
        climateChange = climateChange.toClimateChange()
    )
}

private fun List<TagResponse>.toTags(): List<Tag> {
    return map { tag ->
        Tag(
            text = tag.text,
            description = tag.description,
            link = tag.link
        )
    }
}

private fun List<StatResponse>.toStats(): List<KeyStats.Stat> {
    return map { stat ->
        KeyStats.Stat(
            label = stat.label,
            description = stat.description,
            value = stat.value
        )
    }
}

private fun ClimateChangeResponse.toClimateChange(): ClimateChange {
    return ClimateChange(
        score = score,
        link = link
    )
}

private fun List<AboutResponse>.toAbout(): List<About> {
    return map { about ->
        About(
            title = about.title,
            description = about.description.toDescription(),
            info = about.info.toInfo()
        )
    }
}

private fun AboutResponse.DescriptionResponse.toDescription(): About.Description {
    return About.Description(
        snippet = snippet,
        link = link,
        linkText = linkText
    )
}

private fun List<AboutResponse.InfoResponse>.toInfo(): List<About.Info> {
    return map { info ->
        About.Info(
            label = info.label,
            value = info.value,
            link = info.link
        )
    }
}
