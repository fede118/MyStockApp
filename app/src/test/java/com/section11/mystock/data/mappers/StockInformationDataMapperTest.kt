package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.PriceMovementResponse
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.SummaryResponse
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.Summary
import org.junit.Assert.assertEquals
import org.junit.Test

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

        // StockInformation
        private val EXPECTED_PRICE_MOVEMENT = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE,
            value = PRICE_MOVEMENT_VALUE,
            movement = PRICE_MOVEMENT_MOVEMENT
        )

        private val EXPECTED_SUMMARY = Summary(
            title = SUMMARY_TITLE,
            stock = SUMMARY_STOCK,
            exchange = SUMMARY_EXCHANGE,
            price = SUMMARY_PRICE,
            currency = SUMMARY_CURRENCY,
            priceMovement = EXPECTED_PRICE_MOVEMENT
        )

        val EXPECTED_STOCK_INFORMATION = StockInformation(
            summary = EXPECTED_SUMMARY
        )
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
        val stockInformationResponse = StockInformationResponse(
            summary = summaryResponse
        )

        // When
        val stockInformation = stockInformationResponse.toStockInformation()

        // Then
        assertEquals(EXPECTED_STOCK_INFORMATION, stockInformation)
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
    }
}
