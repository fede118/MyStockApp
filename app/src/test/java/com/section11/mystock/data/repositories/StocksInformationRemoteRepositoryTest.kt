package com.section11.mystock.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.section11.mystock.data.dto.PriceMovementResponse
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.SummaryResponse
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
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
    private val defaultMarket = "market"

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        repository = StocksInformationRemoteRepository(stocksInformationService, apiKey, defaultMarket)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getStockInformation return stock information`() = runTest(testDispatcher) {
        // Given
        val stockInformationResponse = mockStockInformationResponse()
        val response: Response<StockInformationResponse> = mock()
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(stockInformationResponse)
        whenever(stocksInformationService.getStockInformation(any(), any(), any())).thenReturn(response)

        // When
        val result = repository.getStockInformation(symbol)

        // Then
        verify(stocksInformationService).getStockInformation(apiKey, symbol + defaultMarket)

        with(result.summary) {
            assertEquals(stockInformationResponse.summary.title, title)
            assertEquals(stockInformationResponse.summary.stock, stock)
            assertEquals(stockInformationResponse.summary.exchange, exchange)
            assertEquals(stockInformationResponse.summary.price, price)
            assertEquals(stockInformationResponse.summary.currency, currency)
            assertEquals(stockInformationResponse.summary.priceMovement.percentage, priceMovement.percentage, 0.0)
        }
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

    private fun mockStockInformationResponse(): StockInformationResponse {
        val mockResponse: StockInformationResponse = mock()
        val mockSummary: SummaryResponse = mock()
        val mockPriceMovement: PriceMovementResponse = mock()
        whenever(mockResponse.summary).thenReturn(mockSummary)
        whenever(mockSummary.title).thenReturn("Title")
        whenever(mockSummary.stock).thenReturn("Stock")
        whenever(mockSummary.exchange).thenReturn("Exchange")
        whenever(mockSummary.price).thenReturn("Price")
        whenever(mockSummary.currency).thenReturn("Currency")
        whenever(mockSummary.priceMovement).thenReturn(mockPriceMovement)
        whenever(mockPriceMovement.percentage).thenReturn(0.5)
        whenever(mockPriceMovement.value).thenReturn(10.0)
        whenever(mockPriceMovement.movement).thenReturn("Movement")
        return mockResponse
    }
}
