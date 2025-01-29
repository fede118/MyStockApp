package com.section11.mystock.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.StockSearchResponse
import com.section11.mystock.data.mapper.toStockInformation
import com.section11.mystock.data.mapper.toStockSearchResults
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.Response


@ExperimentalCoroutinesApi
class StocksInformationRemoteRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var stocksInformationService: StocksInformationService

    private lateinit var repository: StocksInformationRemoteRepository

    private val symbol = "SYMBOL"
    private val apiKey = "API_KEY"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = StocksInformationRemoteRepository(stocksInformationService, apiKey)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getStockInformation return stock information`() = runTest(testDispatcher) {
        // Given
        val stockInformationResponse: StockInformationResponse = mock()
        mockkStatic("com.section11.mystock.data.mapper.StockInformationDataMapperKt")
        every { stockInformationResponse.toStockInformation() } returns mock()

        val response: Response<StockInformationResponse> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(stockInformationResponse)
        whenever(stocksInformationService.getStockInformation(any(), any(), any())).thenReturn(response)

        // When
        val result = repository.getStockInformation(symbol)

        // Then
        verify(stocksInformationService).getStockInformation(apiKey, symbol)
        assertTrue(result is StockInformation)
        // No need to check for the information being mapped thats already tested in
        // StockInformationDataMapper
    }

    @Test(expected = ResponseBodyNullException::class)
    fun `getStockInformation response body null`() = runTest(testDispatcher) {
        // Given
        val response: Response<StockInformationResponse> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(null)
        whenever(stocksInformationService.getStockInformation(any(), any(), any())).thenReturn(response)

        // When
        repository.getStockInformation("SYMBOL")
    }

    @Test(expected = ApiErrorException::class)
    fun `getStockInformation api exception`() = runTest(testDispatcher) {
        // Given
        val errorResponse = "Not Found".toResponseBody()
        val response = Response.error<StockInformationResponse>(404, errorResponse)
        whenever(stocksInformationService.getStockInformation(any(), any(), any())).thenReturn(response)

        // When
        repository.getStockInformation("SYMBOL")
    }

    @Test
    fun `searchStock return stock search results`() = runTest(testDispatcher) {
        // Given
        val query = "query"
        val mockResponse: StockSearchResponse = mock()
        mockkStatic("com.section11.mystock.data.mapper.SearchStockDataMapperKt")
        every { mockResponse.toStockSearchResults() } returns mock()
        val response: Response<StockSearchResponse> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(mockResponse)
        whenever(stocksInformationService.searchStock(any(), any(), any())).thenReturn(response)

        repository.searchStock(query)

        verify(stocksInformationService).searchStock(apiKey, query)
    }

    @Test(expected = ResponseBodyNullException::class)
    fun `searchStock response body null`() = runTest(testDispatcher) {
        // Given
        val response: Response<StockSearchResponse> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(null)
        whenever(stocksInformationService.searchStock(any(), any(), any())).thenReturn(response)

        // When
        repository.searchStock("SYMBOL")
    }

    @Test(expected = ApiErrorException::class)
    fun `searchStock api exception`() = runTest(testDispatcher) {
        // Given
        val errorResponse = "Not Found".toResponseBody()
        val response = Response.error<StockSearchResponse>(404, errorResponse)
        whenever(stocksInformationService.searchStock(any(), any(), any())).thenReturn(response)

        // When
        repository.searchStock("SYMBOL")
    }
}
