package com.section11.mystock.domain

import com.section11.mystock.data.repositories.StockRepository
import com.section11.mystock.models.Stock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetAllStocksUseCaseTest {

    private lateinit var getAllStocksUseCase: GetAllStocksUseCase
    private lateinit var stockRepository: StockRepository

    @Before
    fun setUp() {
        stockRepository = mock()
        getAllStocksUseCase = GetAllStocksUseCase(stockRepository)
    }

    @Test
    fun `getWatchlist should return stocks from repository`() = runTest {
        // Arrange
        val expectedStocks = listOf(
            Stock(name = "Stock 1", symbol = "STK1"),
            Stock(name = "Stock 2", symbol = "STK2")
        )
        whenever(stockRepository.getAllStocks()).thenReturn(flowOf(expectedStocks))

        // Act
        val actualStocks = getAllStocksUseCase.getWatchlist().first()

        // Assert
        Assert.assertEquals(expectedStocks, actualStocks)
    }

    @Test
    fun `getWatchlist should return empty list if repository returns empty list`() = runTest {
        // Arrange
        val expectedStocks = emptyList<Stock>()
        whenever(stockRepository.getAllStocks()).thenReturn(flowOf(expectedStocks))

        // Act
        val actualStocks = getAllStocksUseCase.getWatchlist().first()

        // Assert
        Assert.assertEquals(expectedStocks, actualStocks)
    }
}