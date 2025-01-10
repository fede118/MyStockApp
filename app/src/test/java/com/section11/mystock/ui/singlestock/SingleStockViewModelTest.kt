package com.section11.mystock.ui.singlestock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.Success
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.Loading
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.Error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SingleStockViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var stocksInformationUseCase: StocksInformationUseCase

    @Mock
    private lateinit var stockInformation: StockInformation

    private lateinit var viewModel: SingleStockViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = SingleStockViewModel(stocksInformationUseCase, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is loading`() = runTest {
        // Expected
        assertEquals(Loading, viewModel.uiState.value)
    }

    @Test
    fun `getStockInformation success`() = runTest(testDispatcher) {
        // Given
        whenever(stocksInformationUseCase.getStockInformation("AAPL"))
            .thenReturn(stockInformation)

        // When
        viewModel.getStockInformation("AAPL")
        advanceUntilIdle()

        // Then
        assertEquals(Success(stockInformation), viewModel.uiState.value)
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
        assertEquals(Error(exception.message), viewModel.uiState.value)
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
        assertEquals(Error(exception.message), viewModel.uiState.value)
    }
}
