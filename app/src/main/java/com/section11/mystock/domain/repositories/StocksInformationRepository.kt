package com.section11.mystock.domain.repositories

import com.section11.mystock.domain.models.StockInformation

interface StocksInformationRepository {

    suspend fun getStockInformation(symbol: String): StockInformation
}
