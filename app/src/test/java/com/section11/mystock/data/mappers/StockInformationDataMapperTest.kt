package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.GraphNodeResponse
import com.section11.mystock.data.dto.PriceMovementResponse
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.SummaryResponse
import com.section11.mystock.domain.models.GraphNodeDate.Companion.GRAPH_NODE_DATE_FORMAT
import com.section11.mystock.domain.models.GraphNodeDate.Companion.SERVICE_RESPONSE_DATE_FORMAT
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.fail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StockInformationDataMapperTest {

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

    @Test
    fun `StockInformationResponse toStockInformation maps correctly`() {
        // Given
        val priceMovementResponse = PriceMovementResponse(
            percentage = PRICE_MOVEMENT_PERCENTAGE,
            value = PRICE_MOVEMENT_VALUE,
            movement = PRICE_MOVEMENT_MOVEMENT
        )
        val summaryResponse = SummaryResponse(
            title = SUMMARY_TITLE,
            stock = SUMMARY_STOCK,
            exchange = SUMMARY_EXCHANGE,
            price = SUMMARY_PRICE,
            currency = SUMMARY_CURRENCY,
            priceMovement = priceMovementResponse
        )
        val listOfDates = getListOfDates()
        val graph = List(GRAPH_LIST_SIZE) { index ->
            GraphNodeResponse(
                price = index.toDouble(),
                date = listOfDates[index]
            )
        }
        val stockInformationResponse = StockInformationResponse(
            summary = summaryResponse,
            graph = graph
        )
        val expectedDateFormat = GRAPH_NODE_DATE_FORMAT
        val dateFormat = SimpleDateFormat(expectedDateFormat, Locale.ENGLISH)

        // When
        val stockInformation = stockInformationResponse.toStockInformation()

        // Then
        with(stockInformation.summary) {
            assertEquals(SUMMARY_TITLE, title)
            assertEquals(SUMMARY_STOCK, stock)
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
    }

    private fun getListOfDates(size: Int = GRAPH_LIST_SIZE): List<String> {
        val dateFormat = SimpleDateFormat(SERVICE_RESPONSE_DATE_FORMAT, Locale.US)
        val calendar = Calendar.getInstance()

        return List(size) {
            calendar.add(Calendar.MINUTE, it)
            dateFormat.format(calendar.time)
        }
    }
}
