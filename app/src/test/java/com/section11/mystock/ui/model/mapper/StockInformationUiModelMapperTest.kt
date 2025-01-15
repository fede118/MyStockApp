package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.GraphInformation
import com.section11.mystock.domain.models.GraphNode
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.Summary
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

private const val DEFAULT_DIGITS = 2
private const val STOCK_TITLE = "Apple Inc."
private const val STOCK_SYMBOL = "AAPL"
private const val STOCK_EXCHANGE = "NASDAQ"
private const val STOCK_PRICE = "150.0"
private const val STOCK_CURRENCY = "USD"
private const val PRICE_MOVEMENT_UP = "Up"
private const val PRICE_MOVEMENT_DOWN = "Down"
private const val PRICE_MOVEMENT_VALUE_UP = 10.0
private const val PRICE_MOVEMENT_VALUE_DOWN = 5.0
private const val PRICE_MOVEMENT_PERCENTAGE_UP = 0.5
private const val PRICE_MOVEMENT_PERCENTAGE_DOWN = 0.25
private const val PRICE_MOVEMENT_TITLE = "Price Movement"
private const val DEFAULT_GRAPH_SIZE = 30

class StockInformationUiModelMapperTest {

    private val mockListOfPrices = List(DEFAULT_GRAPH_SIZE) { it.toDouble() }
    private val mockListOfLabels = listOf("Label 1", "Label 2", "Label 3")

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var mapper: StockInformationUiModelMapper

    @Before
    fun setup() {
        resourceProvider = mock()
        mapper = StockInformationUiModelMapper(resourceProvider)
        whenever(resourceProvider.getString(R.string.single_stock_screen_symbol_text, STOCK_SYMBOL))
            .thenReturn(STOCK_SYMBOL)
        whenever(resourceProvider.getString(R.string.single_stock_screen_exchange_text, STOCK_EXCHANGE))
            .thenReturn(STOCK_EXCHANGE)
        whenever(resourceProvider.getString(R.string.single_stock_screen_price_text, STOCK_PRICE, STOCK_CURRENCY))
            .thenReturn("Price: $STOCK_PRICE $STOCK_CURRENCY")
        whenever(resourceProvider.getString(R.string.single_stock_screen_price_movement_title))
            .thenReturn(PRICE_MOVEMENT_TITLE)
        whenever(resourceProvider.getString(R.string.single_stock_screen_arrow_up))
            .thenReturn("↑")
        whenever(resourceProvider.getString(R.string.single_stock_screen_arrow_down))
            .thenReturn("↓")
        whenever(
            resourceProvider.getString(
                eq(R.string.single_stock_screen_price_movement_label),
                eq("↑"),
                any()
            )
        ).thenReturn("↑ $PRICE_MOVEMENT_VALUE_UP")
        whenever(
            resourceProvider.getString(
                eq(R.string.single_stock_screen_price_movement_label),
                eq("↓"),
                any()
            )
        ).thenReturn("↓ $PRICE_MOVEMENT_VALUE_DOWN")
        whenever(resourceProvider.getString(R.string.single_stock_screen_plus))
            .thenReturn("+")
        whenever(resourceProvider.getString(R.string.single_stock_screen_minus))
            .thenReturn("-")
        whenever(
            resourceProvider.getString(
                R.string.single_stock_screen_price_movement_percentage,
                "+",
                PRICE_MOVEMENT_PERCENTAGE_UP.format(DEFAULT_DIGITS)
            )
        ).thenReturn("+ $PRICE_MOVEMENT_PERCENTAGE_UP%")
        whenever(
            resourceProvider.getString(
                R.string.single_stock_screen_price_movement_percentage,
                "-",
                PRICE_MOVEMENT_PERCENTAGE_DOWN.format(DEFAULT_DIGITS)
            )
        ).thenReturn("- $PRICE_MOVEMENT_PERCENTAGE_DOWN%")
    }

    @Test
    fun `mapToUiModel should map StockInformation to StockInformationUiModel correctly`() {
        // Given
        val priceMovement = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE_UP,
            value = PRICE_MOVEMENT_VALUE_UP,
            movement = PRICE_MOVEMENT_UP
        )
        val summary = Summary(
            title = STOCK_TITLE,
            stock = STOCK_SYMBOL,
            exchange = STOCK_EXCHANGE,
            price = STOCK_PRICE,
            currency = STOCK_CURRENCY,
            priceMovement = priceMovement
        )

        val graphInfo = GraphInformation(
            graphNodes = List(DEFAULT_GRAPH_SIZE) { index ->
                GraphNode(mockListOfPrices[index], "Date $index")
            },
            horizontalAxisLabels = mockListOfLabels,
        )
        val stockInformation = StockInformation(
            summary = summary,
            graph = graphInfo
        )

        // When
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Then
        with(uiModel) {
            assertEquals(STOCK_TITLE, title)
            assertEquals(STOCK_SYMBOL, stockSymbolLabel)
            assertEquals(STOCK_EXCHANGE, exchangeLabel)
            assertEquals("Price: $STOCK_PRICE $STOCK_CURRENCY", priceLabel)
            assertEquals(PRICE_MOVEMENT_TITLE, priceMovementTitle)
            assertEquals("↑ $PRICE_MOVEMENT_VALUE_UP", priceMovementValueLabel)
            assertEquals("+ $PRICE_MOVEMENT_PERCENTAGE_UP%", priceMovementPercentage)
            assertEquals(Green, priceMovementColor)
            assertEquals(mockListOfPrices.size, graphModel.graphPoints.size)
            assertEquals(mockListOfPrices, graphModel.graphPoints)
            assertEquals(mockListOfLabels, graphModel.graphHorizontalLabels)
        }
    }

    @Test
    fun `mapToUiModel should handle price movement Up correctly`() {
        // Arrange
        val priceMovement = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE_UP,
            value = PRICE_MOVEMENT_VALUE_UP,
            movement = PRICE_MOVEMENT_UP
        )
        val summary = Summary(
            title = STOCK_TITLE,
            stock = STOCK_SYMBOL,
            exchange = STOCK_EXCHANGE,
            price = STOCK_PRICE,
            currency = STOCK_CURRENCY,
            priceMovement = priceMovement
        )
        val stockInformation = StockInformation(
            summary = summary,
            graph = mock() // graph mapping is already tested in first test
        )

        // Act
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Assert
        assertEquals("↑ $PRICE_MOVEMENT_VALUE_UP", uiModel.priceMovementValueLabel)
        assertEquals("+ $PRICE_MOVEMENT_PERCENTAGE_UP%", uiModel.priceMovementPercentage)
        assertEquals(Green, uiModel.priceMovementColor)
    }

    @Test
    fun `mapToUiModel should handle price movement Down correctly`() {
        // Arrange
        val priceMovement = PriceMovement(
            percentage = PRICE_MOVEMENT_PERCENTAGE_DOWN,
            value = PRICE_MOVEMENT_VALUE_DOWN,
            movement = PRICE_MOVEMENT_DOWN
        )
        val summary = Summary(
            title = STOCK_TITLE,
            stock = STOCK_SYMBOL,
            exchange = STOCK_EXCHANGE,
            price = STOCK_PRICE,
            currency = STOCK_CURRENCY,
            priceMovement = priceMovement
        )
        val stockInformation = StockInformation(
            summary = summary,
            mock() // graph mapping is already tested in first test
        )

        // Act
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Assert
        assertEquals("↓ $PRICE_MOVEMENT_VALUE_DOWN", uiModel.priceMovementValueLabel)
        assertEquals("- $PRICE_MOVEMENT_PERCENTAGE_DOWN%", uiModel.priceMovementPercentage)
        assertEquals(Red, uiModel.priceMovementColor)
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)
