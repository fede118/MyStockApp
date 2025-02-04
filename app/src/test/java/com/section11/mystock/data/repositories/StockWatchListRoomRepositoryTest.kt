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
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
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

    @Test
    fun `when save stock then should save stock to room db`() = runTest {
        val stock = Stock("name", "symbol", "exchange")
        val captor = argumentCaptor<StockEntity>()
        whenever(stockDao.insert(captor.capture())).thenReturn(Unit)

        repository.saveStock(stock)

        val capturedStock = captor.firstValue
        assertEquals(stock.name, capturedStock.name)
        assertEquals(stock.symbol, capturedStock.symbol)
        assertEquals(stock.exchange, capturedStock.exchange)
    }

    @Test
    fun `when remove stock from watchlist then should remove stock from room db`() = runTest {
        val stockSymbol = "symbol"
        whenever(stockDao.remove(stockSymbol)).thenReturn(Unit)

        repository.removeStockFromWatchlist(stockSymbol)

        verify(stockDao).remove(stockSymbol)
    }

    @Test
    fun `when isStockInWatchlist then should check if stock is in room db`() = runTest {
        val stockSymbol = "symbol"
        val stockEntity = StockEntity(name = "name", symbol = stockSymbol, exchange = "exchange")
        whenever(stockDao.getStockBySymbol(stockSymbol)).thenReturn(stockEntity)

        val result = repository.isStockInWatchlist(stockSymbol)

        assertEquals(true, result)
    }

    @Test
    fun `when isStockInWatchlist then if stock is not there it should return false`() {
        // not setting "whenever(stockDao.getStockBySymbol(stockSymbol))" so that it return null
        val stockSymbol = "symbol"

        val result = repository.isStockInWatchlist(stockSymbol)

        assertEquals(false, result)
    }
}
