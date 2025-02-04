package com.section11.mystock.data.repositories

import com.section11.mystock.data.mapper.toStockInformation
import com.section11.mystock.data.mapper.toStockSearchResults
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.domain.repositories.StocksInformationRepository
import com.section11.mystock.framework.networking.safeApiCall
import javax.inject.Inject

class StocksInformationRemoteRepository @Inject constructor(
    private val stocksInformationService: StocksInformationService,
    private val apiKey: String,
): StocksInformationRepository {

    override suspend fun getStockInformation(symbolColonExchange: String): StockInformation {
        val stockInformationResponse = safeApiCall {
            stocksInformationService.getStockInformation(
                apiKey,
                symbolColonExchange
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
