package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.AboutResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.AboutResponse.DescriptionResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.AboutResponse.InfoResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.ClimateChangeResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.StatResponse
import com.section11.mystock.data.dto.StockInformationResponse.KnowledgeGraphResponse.KeyStatsResponse.TagResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse.PriceMovementResponse
import com.section11.mystock.data.mapper.toStockInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation.Companion.GRAPH_NODE_DATE_FORMAT
import com.section11.mystock.domain.models.StockInformation.GraphInformation.Companion.SERVICE_RESPONSE_DATE_FORMAT
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.fail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StockInformationDataMapperTest {

    private val priceMovementResponse = PriceMovementResponse(
        percentage = PRICE_MOVEMENT_PERCENTAGE,
        value = PRICE_MOVEMENT_VALUE,
        movement = PRICE_MOVEMENT_MOVEMENT
    )
    private val summaryResponse = SummaryResponse(
        title = SUMMARY_TITLE,
        symbol = SUMMARY_STOCK,
        exchange = SUMMARY_EXCHANGE,
        price = SUMMARY_PRICE,
        currency = SUMMARY_CURRENCY,
        priceMovement = priceMovementResponse
    )
    private val listOfDates = getListOfDates()
    private val graph = List(GRAPH_LIST_SIZE) { index ->
        StockInformationResponse.GraphNodeResponse(
            price = index.toDouble(),
            date = listOfDates[index]
        )
    }
    private val knowledgeGraphResponse = KnowledgeGraphResponse(
        keyStats = KeyStatsResponse(
            tags = listOf(
                TagResponse("tag1", "description1"),
                TagResponse("tag2", "description2")
            ),
            stats = listOf(
                StatResponse("label1", "description1", "value1"),
                StatResponse("label2", "description2", "value2")
            ),
            climateChange = ClimateChangeResponse("score1", "link1")
        ),
        about = listOf(
            AboutResponse(
                title = "title",
                description = DescriptionResponse("snippet", "link", "linkText"),
                info = listOf(InfoResponse("label", "value", "link"))
            )
        )
    )

    @Test
    fun `StockInformationResponse toStockInformation maps correctly`() {
        // Given
        val stockInformationResponse = StockInformationResponse(
            summary = summaryResponse,
            graph = graph,
            knowledgeGraph = knowledgeGraphResponse
        )
        val expectedDateFormat = GRAPH_NODE_DATE_FORMAT
        val dateFormat = SimpleDateFormat(expectedDateFormat, Locale.ENGLISH)

        // When
        val stockInformation = stockInformationResponse.toStockInformation()

        // Then
        with(stockInformation.summary) {
            assertEquals(SUMMARY_TITLE, title)
            assertEquals(SUMMARY_STOCK, symbol)
            assertEquals(SUMMARY_EXCHANGE, exchange)
            assertEquals(SUMMARY_PRICE, price)
            assertEquals(SUMMARY_CURRENCY, currency)
        }
        with(stockInformation.summary.priceMovement) {
            assertEquals(PRICE_MOVEMENT_PERCENTAGE, percentage, 0.00)
            assertEquals(PRICE_MOVEMENT_VALUE, value, 0.00)
            assertEquals(PRICE_MOVEMENT_MOVEMENT, movement)
        }
        with(stockInformation.graph) {
            assertEquals(GRAPH_LIST_SIZE, graphNodes.size)
            graphNodes.forEachIndexed { index, node ->
                assertEquals(index.toDouble(), node.price, 0.00)
                try {
                    dateFormat.parse(node.dateLabel)
                } catch (e: Exception) {
                    fail(e)
                }
            }
        }
        with(stockInformation.knowledgeGraph) {
            with(keyStats) {
                assertEquals(knowledgeGraphResponse.keyStats.stats.size, stats.size)
                knowledgeGraphResponse.keyStats.stats.forEachIndexed { index, stat ->
                assertEquals(stat.label, stats[index].label)
                    assertEquals(stat.description, stats[index].description)
                    assertEquals(stat.value, stats[index].value)
                }
                assertEquals(knowledgeGraphResponse.keyStats.tags.size, tags.size)
                knowledgeGraphResponse.keyStats.tags.forEachIndexed { index, tag ->
                    assertEquals(tag.text, tags[index].text)
                    assertEquals(tag.description, tags[index].description)
                    assertEquals(tag.link, tags[index].link)
                }
                assertEquals(knowledgeGraphResponse.keyStats.climateChange?.link, climateChange?.link)
                assertEquals(knowledgeGraphResponse.keyStats.climateChange?.score, climateChange?.score)
            }
            assertEquals(knowledgeGraphResponse.about.size, about.size)
            knowledgeGraphResponse.about.forEachIndexed { index, info ->
                assertEquals(info.title, about[index].title)
                assertEquals(info.description.snippet, about[index].description.snippet)
            }
        }
    }

    private fun getListOfDates(size: Int = GRAPH_LIST_SIZE): List<String> {
        val dateFormat = SimpleDateFormat(SERVICE_RESPONSE_DATE_FORMAT, Locale.US)
        val calendar = Calendar.getInstance()

        return List(size) {
            calendar.add(Calendar.MINUTE, it)
            dateFormat.format(calendar.time)
        }
    }

    companion object {
        // PriceMovement
        const val PRICE_MOVEMENT_PERCENTAGE = 0.5
        const val PRICE_MOVEMENT_VALUE = 10.0
        const val PRICE_MOVEMENT_MOVEMENT = "UP"

        // Summary
        const val SUMMARY_TITLE = "Apple Inc."
        const val SUMMARY_STOCK = "AAPL"
        const val SUMMARY_EXCHANGE = "NASDAQ"
        const val SUMMARY_PRICE = "150.00"
        const val SUMMARY_CURRENCY = "USD"
        const val GRAPH_LIST_SIZE = 30
    }
}
