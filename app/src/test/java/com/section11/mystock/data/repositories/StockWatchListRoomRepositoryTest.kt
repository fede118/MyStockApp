package com.section11.mystock.data.repositories

import com.section11.mystock.data.local.database.daos.StockDao
import com.section11.mystock.data.local.database.entities.StockEntity
import com.section11.mystock.domain.models.Stock
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class StockWatchListRoomRepositoryTest {

    @Mock
    private lateinit var stockDao: StockDao

    private lateinit var repository: RoomStockWatchlistRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = RoomStockWatchlistRepository(stockDao)
    }

    @Test
    fun `getAllStocks returns correct list of stocks`() = runTest {
        // Given
        val stockEntities = listOf(
            StockEntity(name = "Stock 1", symbol = "STK1", exchange = "NASDAQ"),
            StockEntity(name = "Stock 2", symbol = "STK2", exchange = "NYSE")
        )
        val expectedStocks = listOf(
            Stock(name = "Stock 1", symbol = "STK1", exchange = "NASDAQ"),
            Stock(name = "Stock 2", symbol = "STK2", exchange = "NYSE")
        )
        whenever(stockDao.getStocksWatchlist()).thenReturn(flowOf(stockEntities))

        // When
        val actualStocks = repository.getAllStocks().first()

        // Then
        assertEquals(expectedStocks, actualStocks)
    }

    @Test
    fun `getAllStocks returns empty list when no stocks in database`() = runTest {
        // Given
        whenever(stockDao.getStocksWatchlist()).thenReturn(flowOf(emptyList()))

        // When
        val actualStocks = repository.getAllStocks().first()

        // Then
        assertEquals(emptyList<Stock>(), actualStocks)
    }
}
