package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
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

class StockInformationUiModelMapperTest {

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
            summary = summary
        )

        // Act
        val uiModel = mapper.mapToUiModel(stockInformation)

        // Assert
        assertEquals(STOCK_TITLE, uiModel.title)
        assertEquals(STOCK_SYMBOL, uiModel.stockSymbolLabel)
        assertEquals(STOCK_EXCHANGE, uiModel.exchangeLabel)
        assertEquals("Price: $STOCK_PRICE $STOCK_CURRENCY", uiModel.priceLabel)
        assertEquals(PRICE_MOVEMENT_TITLE, uiModel.priceMovementTitle)
        assertEquals("↑ $PRICE_MOVEMENT_VALUE_UP", uiModel.priceMovementValueLabel)
        assertEquals("+ $PRICE_MOVEMENT_PERCENTAGE_UP%", uiModel.priceMovementPercentage)
        assertEquals(Green, uiModel.priceMovementColor)
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
            summary = summary
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
            summary = summary
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
