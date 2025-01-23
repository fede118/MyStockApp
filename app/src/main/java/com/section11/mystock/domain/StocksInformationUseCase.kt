package com.section11.mystock.domain

import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.domain.repositories.StocksInformationRepository
import javax.inject.Inject

class StocksInformationUseCase @Inject constructor(
    private val stocksInformationRepository: StocksInformationRepository
) {

    suspend fun getStockInformation(symbol: String): StockInformation {
        return stocksInformationRepository.getStockInformation(symbol)
    }

    @Suppress("SwallowedException")
    // This exception is thrown by our service, and we expect a null body if there are no search results
    suspend fun searchStock(query: String): StockSearchResults? {
        return try {
            stocksInformationRepository.searchStock(query)
        } catch (nullBody: ResponseBodyNullException) {
            null
        }
    }
}
