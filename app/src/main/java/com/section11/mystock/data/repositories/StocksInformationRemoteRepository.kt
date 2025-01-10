package com.section11.mystock.data.repositories

import com.section11.mystock.data.mappers.toStockInformation
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.repositories.StocksInformationRepository
import javax.inject.Inject

class StocksInformationRemoteRepository @Inject constructor(
    private val stocksInformationService: StocksInformationService,
    private val apiKey: String,
    private val market: String
): StocksInformationRepository {

    override suspend fun getStockInformation(symbol: String): StockInformation {
        val response = stocksInformationService.getStockInformation(apiKey, symbol + market)

        if (response.isSuccessful) {
            val stockInformationResponse = response.body()
            if (stockInformationResponse != null) {
                return stockInformationResponse.toStockInformation()
            } else {
                throw ResponseBodyNullException()
            }
        } else {
            throw ApiErrorException(response.code(), response.errorBody()?.string())
        }
    }
}
