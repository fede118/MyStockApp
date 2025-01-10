package com.section11.mystock.domain.watchlist

import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockWatchlistUseCase @Inject constructor(private val stockWatchlistRepository: StockWatchlistRepository) {

    fun getWatchlist(): Flow<List<Stock>> {
        return stockWatchlistRepository.getAllStocks()
    }
}
