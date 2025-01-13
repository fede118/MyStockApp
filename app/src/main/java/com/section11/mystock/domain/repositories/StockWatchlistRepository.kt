package com.section11.mystock.domain.repositories

import com.section11.mystock.domain.models.Stock
import kotlinx.coroutines.flow.Flow

interface StockWatchlistRepository {
    fun getAllStocks(): Flow<List<Stock>>
}