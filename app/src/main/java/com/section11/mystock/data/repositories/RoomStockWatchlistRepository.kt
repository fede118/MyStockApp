package com.section11.mystock.data.repositories

import com.section11.mystock.data.local.database.daos.StockDao
import com.section11.mystock.data.local.database.entities.StockEntity
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import com.section11.mystock.domain.models.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomStockWatchlistRepository @Inject constructor(
    private val stockDao: StockDao
): StockWatchlistRepository {

    override fun getAllStocks(): Flow<List<Stock>> {
        return stockDao.getStocksWatchlist().map { stockEntities ->
            stockEntities.map { it.toStock() }
        }
    }

    private fun StockEntity.toStock(): Stock {
        return Stock(
            name = name,
            symbol = symbol
        )
    }
}
