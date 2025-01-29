package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.CloseMatchPriceMovementResponse
import com.section11.mystock.data.dto.CloseMatchStockResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse
import com.section11.mystock.data.dto.StockInformationResponse.SummaryResponse.PriceMovementResponse
import com.section11.mystock.data.dto.StockSearchResponse
import com.section11.mystock.data.mapper.toCloseMatchStock
import com.section11.mystock.data.mapper.toCloseMatchStockPriceMovement
import com.section11.mystock.data.mapper.toStockSearchResults
import com.section11.mystock.domain.models.CloseMatchStock
import com.section11.mystock.domain.models.CloseMatchStockPriceMovement
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.domain.models.StockInformation.Summary.PriceMovement
import com.section11.mystock.domain.models.StockSearchResults
import org.junit.Assert.assertEquals
import org.junit.Test

class StockSearchResponseMapperTest {

    @Test
    fun `toStockSearchResults maps StockSearchResponse to StockSearchResults correctly`() {
        // Given
        val summaryResponse = SummaryResponse(
            title = "Apple Inc.",
            stock = "AAPL",
            price = "150.00",
            currency = "$",
            exchange = "exchange",
            priceMovement = PriceMovementResponse(2.5, 5.0, "Up")
        )
        val closeMatchStockResponse = listOf(
            CloseMatchStockResponse(
                title = "Microsoft Corp.",
                stock = "MSFT",
                extractedPrice = "310.00",
                currency = "$",
                priceMovement = CloseMatchPriceMovementResponse(-1.2, "Down")
            )
        )
        val stockSearchResponse = StockSearchResponse(
            summary = summaryResponse,
            closeMatchStock = closeMatchStockResponse
        )

        // When
        val result = stockSearchResponse.toStockSearchResults()

        // Then
        val expected = StockSearchResults(
            exactMatch = Summary(
                title = "Apple Inc.",
                stock = "AAPL",
                price = "150.00",
                currency = "$",
                exchange = "exchange",
                priceMovement = PriceMovement(2.5, 5.0, "Up")
            ),
            closeMatchStocks = listOf(
                CloseMatchStock(
                    title = "Microsoft Corp.",
                    stock = "MSFT",
                    extractedPrice = "310.00",
                    currency = "$",
                    priceMovement = CloseMatchStockPriceMovement(-1.2, "Down")
                )
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toStockSearchResults handles null values correctly`() {
        // Given
        val stockSearchResponse = StockSearchResponse(
            summary = null,
            closeMatchStock = null
        )

        // When
        val result = stockSearchResponse.toStockSearchResults()

        // Then
        val expected = StockSearchResults(
            exactMatch = null,
            closeMatchStocks = null
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toCloseMatchStock maps CloseMatchStockResponse to CloseMatchStock correctly`() {
        // Given
        val closeMatchStockResponse = CloseMatchStockResponse(
            title = "Microsoft Corp.",
            stock = "MSFT",
            extractedPrice = "310.00",
            currency = "$",
            priceMovement = CloseMatchPriceMovementResponse(-1.2, "Down")
        )

        // When
        val result = closeMatchStockResponse.toCloseMatchStock()

        // Then
        val expected = CloseMatchStock(
            title = "Microsoft Corp.",
            stock = "MSFT",
            extractedPrice = "310.00",
            currency = "$",
            priceMovement = CloseMatchStockPriceMovement(-1.2, "Down")
        )
        assertEquals(expected, result)
    }

    @Test
    fun `toCloseMatchStockPriceMovement maps CloseMatchPriceMovementResponse correctly`() {
        // Given
        val priceMovementResponse = CloseMatchPriceMovementResponse(
            percentage = 2.5,
            movement = "Up"
        )

        // When
        val result = priceMovementResponse.toCloseMatchStockPriceMovement()

        // Then
        val expected = CloseMatchStockPriceMovement(
            percentage = 2.5,
            movement = "Up"
        )
        assertEquals(expected, result)
    }
}
