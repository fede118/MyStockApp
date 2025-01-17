package com.section11.mystock.ui.home

import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.domain.models.Stock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: HomeViewModel
    private lateinit var stockWatchlistUseCase: StockWatchlistUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        stockWatchlistUseCase = mock()
        viewModel = HomeViewModel(stockWatchlistUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Expected
        Assert.assertEquals(HomeUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `getStocks success`() = runTest {
        // Given
        val expectedStocks = listOf(
            Stock(name = "Stock 1", symbol = "STK1"),
            Stock(name = "Stock 2", symbol = "STK2")
        )
        whenever(stockWatchlistUseCase.getWatchlist()).thenReturn(flowOf(expectedStocks))

        // When
        viewModel.getStocks()
        advanceUntilIdle()

        // Then
        verify(stockWatchlistUseCase).getWatchlist()
        Assert.assertEquals(HomeUiState.Success(expectedStocks), viewModel.uiState.value)
    }
}
