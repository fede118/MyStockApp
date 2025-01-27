package com.section11.mystock.domain

import com.section11.mystock.domain.common.Const.COLON
import com.section11.mystock.domain.common.Const.DEFAULT_EXCHANGE_MARKET
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.domain.repositories.StocksInformationRepository
import javax.inject.Inject
import javax.inject.Named

class StocksInformationUseCase @Inject constructor(
    private val stocksInformationRepository: StocksInformationRepository,
    @Named(DEFAULT_EXCHANGE_MARKET) private val defaultExchange: String
) {

    suspend fun getStockInformation(symbol: String, exchange: String? = defaultExchange): StockInformation {
        val symbolColonExchange = "$symbol$COLON$exchange"
        return stocksInformationRepository.getStockInformation(symbolColonExchange)
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
