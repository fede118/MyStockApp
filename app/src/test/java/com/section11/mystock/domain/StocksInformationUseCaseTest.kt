package com.section11.mystock.domain

import com.section11.mystock.domain.repositories.StocksInformationRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify

class StocksInformationUseCaseTest {

    private val stocksInformationRepository: StocksInformationRepository = mock()

    private lateinit var stocksInformationUseCase: StocksInformationUseCase

    @Before
    fun setUp() {
        stocksInformationUseCase = StocksInformationUseCase(stocksInformationRepository)
    }

    @Test
    fun `getStockInformation calls repository`() = runTest {
        val symbol = "symbol"
        stocksInformationUseCase.getStockInformation(symbol)

        verify(stocksInformationRepository).getStockInformation(symbol)
    }
}
