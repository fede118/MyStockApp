package com.section11.mystock.ui.home

import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.WatchlistFetched
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.GetSingleStockInfo
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val stockWatchlistUiModelMapper: StockWatchlistUiModelMapper = mock()
    private val stocksInformationUseCase: StocksInformationUseCase = mock()
    private val stockInformationUiModelMapper: StockInformationUiModelMapper = mock()
    private val featureFlagManager: FeatureFlagManager = mock()
    private val stocks = listOf(
        Stock(name = "Stock 1", symbol = "STK1"),
        Stock(name = "Stock 2", symbol = "STK2")
    )
    private val expectedStocks = listOf(
        WatchlistStockModel(stockTitle = "Stock 1", symbol = "STK1", "5%", Green),
        WatchlistStockModel(stockTitle = "Stock 2", symbol = "STK2", "-2%", Red)
    )
    private val uiModel = WatchlistScreenUiModel(
        searchHint = "Search",
        stocks = expectedStocks
    )

    private lateinit var viewModel: HomeViewModel
    private lateinit var stockWatchlistUseCase: StockWatchlistUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        stockWatchlistUseCase = mock()
        // Given
        whenever(stockWatchlistUseCase.getWatchlist()).thenReturn(flowOf(stocks))
        whenever(stockWatchlistUiModelMapper.mapToUiModel(any())).thenReturn(uiModel)

        viewModel = HomeViewModel(
            stockWatchlistUseCase,
            stockWatchlistUiModelMapper,
            featureFlagManager,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getStocks success`() = runTest {
        // When
        advanceUntilIdle() // getStocks is called on init of viewModel

        // Then
        verify(stockWatchlistUseCase).getWatchlist()
        Assert.assertEquals(WatchlistFetched(uiModel), viewModel.uiState.value)
    }

    @Test
    fun `onStockTap with valid symbol`() = runTest {
        // Given
        whenever(featureFlagManager.isNavigationToSingleStockEnabled()).thenReturn(true)
        val symbol = "AAPL"
        val events = mutableListOf<HomeViewModel.NavigationEvent>()

        // Collect the SharedFlow in a coroutine
        val job = launch {
            viewModel.navigationEvent.collect { event ->
                events.add(event)
            }
        }

        // when
        viewModel.onStockTap(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(1, events.size)
        val event = events.first() as HomeViewModel.NavigationEvent.ToSingleStock
        assertEquals(symbol, event.symbol)

        // Cleanup
        job.cancel()
    }

    @Test
    fun `onStockTap with invalid symbol`() = runTest {
        // Given
        whenever(featureFlagManager.isNavigationToSingleStockEnabled()).thenReturn(true)

        // When
        viewModel.onStockTap(null)
        advanceUntilIdle()

        assert(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `onStockTap with Navigation Feature Flag Enabled`() = runTest {
        // Given
        whenever(featureFlagManager.isNavigationToSingleStockEnabled()).thenReturn(true)
        val symbol = "AAPL"
        val events = mutableListOf<HomeViewModel.NavigationEvent>()

        // Collect the SharedFlow in a coroutine
        val job = launch {
            viewModel.navigationEvent.collect { event ->
                events.add(event)
            }
        }

        // when
        viewModel.onStockTap(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(1, events.size)
        val event = events.first() as HomeViewModel.NavigationEvent.ToSingleStock
        assertEquals(symbol, event.symbol)

        // Cleanup
        job.cancel()
    }

    @Test
    fun `onStockTap with Navigation Feature Flag Disabled`() = runTest {
        // Given
        val mockStockInfo: StockInformation = mock()
        val mockStockInfoModel: StockInformationUiModel = mock()
        whenever(featureFlagManager.isNavigationToSingleStockEnabled()).thenReturn(false)
        whenever(stocksInformationUseCase.getStockInformation(any())).thenReturn(mockStockInfo)
        whenever(stockInformationUiModelMapper.mapToUiModel(mockStockInfo)).thenReturn(mockStockInfoModel)
        val symbol = "AAPL"
        val events = mutableListOf<HomeViewModel.NavigationEvent>()

        // Collect the SharedFlow in a coroutine
        val job = launch {
            viewModel.navigationEvent.collect { event ->
                events.add(event)
            }
        }

        // when
        viewModel.onStockTap(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(1, events.size)
        val event = events.first() as GetSingleStockInfo
        assertEquals(symbol, event.symbol)

        // Cleanup
        job.cancel()
    }
}
