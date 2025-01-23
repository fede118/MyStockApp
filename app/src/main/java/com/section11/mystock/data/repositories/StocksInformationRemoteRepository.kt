package com.section11.mystock.data.repositories

import com.section11.mystock.data.mappers.toStockInformation
import com.section11.mystock.data.mappers.toStockSearchResults
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.domain.repositories.StocksInformationRepository
import com.section11.mystock.framework.networking.safeApiCall
import javax.inject.Inject

class StocksInformationRemoteRepository @Inject constructor(
    private val stocksInformationService: StocksInformationService,
    private val apiKey: String,
    private val market: String
): StocksInformationRepository {

    override suspend fun getStockInformation(symbol: String): StockInformation {
        val stockInformationResponse = safeApiCall {
            stocksInformationService.getStockInformation(
                apiKey,
                symbol + market
            )
        }

        return stockInformationResponse.toStockInformation()
    }

    override suspend fun searchStock(query: String): StockSearchResults {
        val stockSearchResponse = safeApiCall {
            stocksInformationService.searchStock(apiKey, query)
        }

        return stockSearchResponse.toStockSearchResults()
    }
}
