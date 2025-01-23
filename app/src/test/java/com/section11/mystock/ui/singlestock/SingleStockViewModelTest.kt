package com.section11.mystock.ui.singlestock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
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
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SingleStockViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SingleStockViewModel

    private val stocksInformationUseCase: StocksInformationUseCase = mock()
    private val stockInformation: StockInformation = mock()
    private val stockInformationUiModel: StockInformationUiModel = mock()
    private val stockInformationUiModelMapper: StockInformationUiModelMapper = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SingleStockViewModel(
            stocksInformationUseCase,
            stockInformationUiModelMapper,
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
        whenever(stocksInformationUseCase.getStockInformation("AAPL"))
            .thenReturn(stockInformation)
        whenever(stockInformationUiModelMapper.mapToUiModel(any()))
            .thenReturn(stockInformationUiModel)

        // When
        viewModel.getStockInformation("AAPL")
        advanceUntilIdle()

        // Then
        assertEquals(SingleStockFetched(stockInformationUiModel), viewModel.uiState.value)
    }

    @Test
    fun `getStockInformation api exception`() = runTest(testDispatcher) {
        // Given
        val exception = ApiErrorException(404, "Not Found")
        whenever(stocksInformationUseCase.getStockInformation("AAPL")).thenThrow(exception)

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
        whenever(stocksInformationUseCase.getStockInformation("AAPL")).thenThrow(exception)

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
}
