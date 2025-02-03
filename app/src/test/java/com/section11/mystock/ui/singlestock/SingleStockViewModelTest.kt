package com.section11.mystock.ui.singlestock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.model.ActionableIconUiModel
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AddToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AlreadyAddedToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.AddToWatchlistTap
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.RemoveFromWatchlistTap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SingleStockViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SingleStockViewModel

    private val stocksInfoUseCase: StocksInformationUseCase = mock()
    private val stockInformation: StockInformation = mock()
    private val stockInformationUiModel: StockInformationUiModel = mock()
    private val mockUiMapper: StockInformationUiModelMapper = mock()
    private val stockWatchlistUseCase: StockWatchlistUseCase = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SingleStockViewModel(
            stocksInfoUseCase,
            mockUiMapper,
            stockWatchlistUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Expected
        assertEquals(UiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `getStockInformation success`() = runTest(testDispatcher) {
        // Given
        val symbol = "AAPL"
        whenever(stocksInfoUseCase.getStockInformation(symbol, null)).thenReturn(stockInformation)
        val summaryMock: StockInformation.Summary = mock()
        whenever(summaryMock.symbol).thenReturn(symbol)
        whenever(stockInformation.summary).thenReturn(summaryMock)
        whenever(mockUiMapper.mapToUiModel(any())).thenReturn(stockInformationUiModel)
        whenever(stockWatchlistUseCase.isStockInWatchlist(any())).thenReturn(true)
        whenever(mockUiMapper.getActionableIconUiModel(any(), any()))
            .thenReturn(AlreadyAddedToWatchlist(getMockActionableIconUiModel()))

        // When
        viewModel.getStockInformation(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(SingleStockFetched(stockInformationUiModel), viewModel.uiState.value)
    }

    @Test
    fun `when stock is already in watchlist should update icon state`() = runTest(testDispatcher) {
        // Given
        val symbol = "AAPL"
        val iconUiModel = AlreadyAddedToWatchlist(getMockActionableIconUiModel())
        whenever(stocksInfoUseCase.getStockInformation(symbol, null)).thenReturn(stockInformation)
        val summaryMock: StockInformation.Summary = mock()
        whenever(summaryMock.symbol).thenReturn(symbol)
        whenever(stockInformation.summary).thenReturn(summaryMock)
        whenever(mockUiMapper.mapToUiModel(any())).thenReturn(stockInformationUiModel)
        whenever(stockWatchlistUseCase.isStockInWatchlist(any())).thenReturn(true)
        whenever(mockUiMapper.getActionableIconUiModel(any(), any())).thenReturn(iconUiModel)

        // When
        viewModel.getStockInformation(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(iconUiModel, viewModel.actionableIconState.value)
    }

    @Test
    fun `when stock is NOT in watchlist should update icon state`() = runTest(testDispatcher) {
        // Given
        val symbol = "AAPL"
        val iconUiModel = AddToWatchlist(getMockActionableIconUiModel())
        whenever(stocksInfoUseCase.getStockInformation(symbol, null)).thenReturn(stockInformation)
        val summaryMock: StockInformation.Summary = mock()
        whenever(summaryMock.symbol).thenReturn(symbol)
        whenever(stockInformation.summary).thenReturn(summaryMock)
        whenever(mockUiMapper.mapToUiModel(any())).thenReturn(stockInformationUiModel)
        whenever(stockWatchlistUseCase.isStockInWatchlist(any())).thenReturn(true)
        whenever(mockUiMapper.getActionableIconUiModel(any(), any())).thenReturn(iconUiModel)

        // When
        viewModel.getStockInformation(symbol)
        advanceUntilIdle()

        // Then
        assertEquals(iconUiModel, viewModel.actionableIconState.value)
    }

    @Test
    fun `getStockInformation api exception`() = runTest(testDispatcher) {
        // Given
        val exception = ApiErrorException(404, "Not Found")
        whenever(stocksInfoUseCase.getStockInformation("AAPL", null)).thenThrow(exception)

        // When
        viewModel.getStockInformation("AAPL")
        advanceUntilIdle()

        // Then
        assertEquals(UiState.Error(exception.message), viewModel.uiState.value)
    }

    @Test
    fun `getStockInformation response body null exception`() = runTest(testDispatcher) {
        // Given
        val exception = ResponseBodyNullException("Response body is null")
        whenever(stocksInfoUseCase.getStockInformation("AAPL", null)).thenThrow(exception)

        // When
        viewModel.getStockInformation("AAPL")
        advanceUntilIdle()

        // Then
        assertEquals(UiState.Error(exception.message), viewModel.uiState.value)
    }

    @Test
    fun `getStockInformation with null symbol should return error`() = runTest(testDispatcher) {
        // When
        viewModel.getStockInformation(null)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `on Single stock event if its not SingleStockScreenEvent should do nothing`() = runTest(testDispatcher) {
        val event = WatchlistScreenEvent.SearchBarClosed
        val currentUiState = viewModel.actionableIconState.value

        viewModel.onSingleStockScreenEvent(event)

        assertEquals(currentUiState, viewModel.actionableIconState.value)
    }

    @Test
    fun `when onSingleScreen event is addToWatchlistTap then should updateIconState`() = runTest {
        val iconUiModel = getMockActionableIconUiModel()
        whenever(stockWatchlistUseCase.saveStockToWatchlist(any())).thenReturn(Unit)
        whenever(mockUiMapper.updateIconOnUiModel(any())).thenReturn(iconUiModel)

        viewModel.onSingleStockScreenEvent(AddToWatchlistTap(iconUiModel))
        advanceUntilIdle()

        verify(mockUiMapper).updateIconOnUiModel(iconUiModel)
        assertEquals(viewModel.actionableIconState.value, AlreadyAddedToWatchlist(iconUiModel))
    }

    @Test
    fun `when onSingleScreen event is RemoveFromWatchlistTap then should updateIconState`() = runTest {
        val iconUiModel = getMockActionableIconUiModel()
        whenever(stockWatchlistUseCase.removeStockFromWatchlist(any())).thenReturn(Unit)
        whenever(mockUiMapper.updateIconOnUiModel(any())).thenReturn(iconUiModel)

        viewModel.onSingleStockScreenEvent(RemoveFromWatchlistTap(iconUiModel))
        advanceUntilIdle()

        verify(mockUiMapper).updateIconOnUiModel(iconUiModel)
        assertEquals(viewModel.actionableIconState.value, AddToWatchlist(iconUiModel))
    }

    private fun getMockActionableIconUiModel(
        stockSymbol: String = "A",
        title: String = "title",
        exchange: String = "Exchange"
    ): ActionableIconUiModel {
        return ActionableIconUiModel(
            title = title,
            symbol = stockSymbol,
            exchange = exchange,
            iconVector = Icons.Default.AddCircle,
            contentDescription = stockSymbol
        )
    }
}
