package com.section11.mystock.domain

import com.section11.mystock.data.repositories.StockRepository
import com.section11.mystock.models.Stock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllStocksUseCase @Inject constructor(private val stockRepository: StockRepository) {
    fun getWatchlist(): Flow<List<Stock>> {
        return stockRepository.getAllStocks()
    }
}