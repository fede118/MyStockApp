package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.Stock
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class StockWatchlistUiModelMapperTest {

    private val resourceProvider: ResourceProvider = mock()

    private lateinit var mapper: StockWatchlistUiModelMapper

    @Before
    fun setup() {
        mapper = StockWatchlistUiModelMapper(resourceProvider)
    }

    @Test
    fun `mapToUiModel converts stocks to watchlist stock models`() {
        val formatedStringMock = "Stock 1 (STK1)"
        whenever(resourceProvider.getString(any(), any(), any())).thenReturn(formatedStringMock)
        whenever(resourceProvider.getString(R.string.watchlist_search_hint)).thenReturn("hint")
        whenever(resourceProvider.getString(eq(R.string.with_percentage),any()))
            .thenReturn("5%")
        val name1 = "Stock 1"
        val name2 = "Stock 2"
        val symbol1 = "STK1"
        val symbol2 = "STK2"

        val mockStocksList = listOf(
            Stock(name1, symbol1),
            Stock(name2, symbol2)
        )

        // Then
        val result = mapper.mapToUiModel(mockStocksList)

        assertEquals(symbol1, result.stocks[0].symbol)
        assertEquals(symbol2, result.stocks[1].symbol)
        assertEquals(formatedStringMock, result.stocks[0].stockTitle)
        assertEquals(formatedStringMock, result.stocks[1].stockTitle)
        assertTrue(result.stocks.size == mockStocksList.size)
    }
}
