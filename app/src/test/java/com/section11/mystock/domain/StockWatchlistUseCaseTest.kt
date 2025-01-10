package com.section11.mystock.domain

import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class StockWatchlistUseCaseTest {

    private lateinit var stockWatchlistUseCase: StockWatchlistUseCase
    private lateinit var stockWatchlistRepository: StockWatchlistRepository

    @Before
    fun setUp() {
        stockWatchlistRepository = mock()
        stockWatchlistUseCase = StockWatchlistUseCase(stockWatchlistRepository)
    }

    @Test
    fun `getWatchlist should return stocks from repository`() = runTest {
        // Arrange
        val expectedStocks = listOf(
            Stock(name = "Stock 1", symbol = "STK1"),
            Stock(name = "Stock 2", symbol = "STK2")
        )
        whenever(stockWatchlistRepository.getAllStocks()).thenReturn(flowOf(expectedStocks))

        // Act
        val actualStocks = stockWatchlistUseCase.getWatchlist().first()

        // Assert
        Assert.assertEquals(expectedStocks, actualStocks)
    }

    @Test
    fun `getWatchlist should return empty list if repository returns empty list`() = runTest {
        // Arrange
        val expectedStocks = emptyList<Stock>()
        whenever(stockWatchlistRepository.getAllStocks()).thenReturn(flowOf(expectedStocks))

        // Act
        val actualStocks = stockWatchlistUseCase.getWatchlist().first()

        // Assert
        Assert.assertEquals(expectedStocks, actualStocks)
    }
}
