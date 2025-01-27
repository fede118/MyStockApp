package com.section11.mystock.domain

import com.section11.mystock.domain.common.Const.COLON
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.repositories.StocksInformationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class StocksInformationUseCaseTest {

    private val stocksInformationRepository: StocksInformationRepository = mock()
    private val defaultExchange = "defaultExchange"

    private lateinit var stocksInformationUseCase: StocksInformationUseCase

    @Before
    fun setUp() {
        stocksInformationUseCase = StocksInformationUseCase(
            stocksInformationRepository,
            defaultExchange
        )
    }

    @Test
    fun `getStockInformation calls repository with default exchange if non is provided`() = runTest {
        val symbol = "symbol"
        stocksInformationUseCase.getStockInformation(symbol)

        verify(stocksInformationRepository)
            .getStockInformation(symbol + COLON + defaultExchange)
    }

    @Test
    fun `getStockInformation calls repository with provided exchange`() = runTest {
        val symbol = "symbol"
        val exchange = "exchange"
        stocksInformationUseCase.getStockInformation(symbol, exchange)

        verify(stocksInformationRepository)
            .getStockInformation(symbol + COLON + exchange)
    }

    @Test
    fun `searchStock calls repository`() = runTest {
        val query = "query"
        stocksInformationUseCase.searchStock(query)

        verify(stocksInformationRepository).searchStock(query)
    }

    @Test
    fun `searchStock calls repository return empty body`() = runTest {
        val query = "query"
        whenever(stocksInformationRepository.searchStock(query)).thenThrow(ResponseBodyNullException())

        val result = stocksInformationUseCase.searchStock(query)

        assert(result == null)
    }
}
