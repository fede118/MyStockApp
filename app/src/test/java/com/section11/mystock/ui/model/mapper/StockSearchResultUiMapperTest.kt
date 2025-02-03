package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.CloseMatchStock
import com.section11.mystock.domain.models.CloseMatchStockPriceMovement
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.domain.models.StockInformation.Summary.PriceMovement
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.ui.common.extentions.toPercentageFormat
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import com.section11.mystock.ui.theme.RedIntense
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class StockSearchResultUiMapperTest {

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var mapper: StockSearchResultUiMapper

    @Before
    fun setUp() {
        resourceProvider = mock()
        mapper = StockSearchResultUiMapper(resourceProvider)
    }

    @Test
    fun `mapToUiModel should map exactMatch correctly`() {
        // Given
        val expectedTitle = "Apple Inc."
        val expectedSymbol = "AAPL"
        val expectedCurrency = "$"
        val expectedPrice = "150.00"
        val expectedExchange = "exchange"
        val expectedPercentage = 5.00
        val expectedPercentageValue = 2.5
        val expectedMovement = "Up"

        val exactMatch = Summary(
            title = expectedTitle,
            symbol = expectedSymbol,
            currency = expectedCurrency,
            price = expectedPrice,
            exchange = expectedExchange,
            priceMovement = PriceMovement(
                expectedPercentage,
                expectedPercentageValue,
                expectedMovement
            )
        )
        val stockSearchResults = StockSearchResults(
            exactMatch = exactMatch,
            closeMatchStocks = emptyList()
        )

        whenever(resourceProvider.getString(R.string.search_bar_results_up_arrow))
            .thenReturn("↑")
        whenever(resourceProvider.getString(R.string.with_percentage, "2.50"))
            .thenReturn("2.50%")

        // When
        val result = mapper.mapToUiModel(stockSearchResults)

        // Then
        val expected = listOf(
            StockSearchResultUiModel(
                title = expectedTitle,
                symbol = expectedSymbol,
                exchange = expectedExchange,
                symbolBoxColor = RedIntense,
                priceLabel = expectedCurrency + expectedPrice,
                priceMovementSymbol = "↑",
                priceMovementColor = Green,
                percentage = expectedPercentage.toPercentageFormat() + "%"
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `mapToUiModel should map closeMatchStocks correctly`() {
        // Given
        val closeMatchStocks = listOf(
            CloseMatchStock(
                title = "Microsoft Corp.",
                symbol = "MSFT",
                exchange = "NASDAQ",
                currency = "$",
                extractedPrice = "310.00",
                priceMovement = CloseMatchStockPriceMovement(-1.2, "Down")
            )
        )
        val stockSearchResults = StockSearchResults(
            exactMatch = null,
            closeMatchStocks = closeMatchStocks
        )

        whenever(resourceProvider.getString(R.string.search_bar_results_down_arrow)).thenReturn("↓")
        whenever(resourceProvider.getString(R.string.with_percentage, "-1.20")).thenReturn("-1.20%")

        // When
        val result = mapper.mapToUiModel(stockSearchResults)

        // Then
        val expected = listOf(
            StockSearchResultUiModel(
                title = "Microsoft Corp.",
                symbol = "MSFT",
                exchange = "NASDAQ",
                symbolBoxColor = RedIntense,
                priceLabel = "$310.00",
                priceMovementSymbol = "↓",
                priceMovementColor = Red,
                percentage = "-1.20%"
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `mapToUiModel should handle null input`() {
        // when
        val result = mapper.mapToUiModel(null)

        // Then
        assertEquals(emptyList<StockSearchResultUiModel>(), result)
    }

    @Test
    fun `mapToUiModel should handle empty exactMatch and closeMatchStocks`() {
        // Given
        val stockSearchResults = StockSearchResults(
            exactMatch = null,
            closeMatchStocks = emptyList()
        )

        // When
        val result = mapper.mapToUiModel(stockSearchResults)

        // Then
        assertEquals(emptyList<StockSearchResultUiModel>(), result)
    }
}

