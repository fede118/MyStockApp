package com.section11.mystock.domain.watchlist

import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockWatchlistUseCase @Inject constructor(
    private val stockWatchlistRepository: StockWatchlistRepository
) {

    fun getWatchlist(): Flow<List<Stock>> {
        return stockWatchlistRepository.getAllStocks()
    }

    fun isStockInWatchlist(symbol: String): Boolean {
        return stockWatchlistRepository.isStockInWatchlist(symbol)
    }

    suspend fun saveStockToWatchlist(stock: Stock) {
        stockWatchlistRepository.saveStock(stock)
    }

    suspend fun removeStockFromWatchlist(symbol: String) {
        return stockWatchlistRepository.removeStockFromWatchlist(symbol)
    }
}
