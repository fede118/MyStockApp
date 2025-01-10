package com.section11.mystock.domain

import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.repositories.StocksInformationRepository
import javax.inject.Inject

class StocksInformationUseCase @Inject constructor(
    private val stocksInformationRepository: StocksInformationRepository
) {

    suspend fun getStockInformation(symbol: String): StockInformation {
        return stocksInformationRepository.getStockInformation(symbol)
    }
}
