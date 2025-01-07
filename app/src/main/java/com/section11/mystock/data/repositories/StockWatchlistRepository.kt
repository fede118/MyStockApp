package com.section11.mystock.data.repositories

import com.section11.mystock.data.local.daos.StockDao
import com.section11.mystock.data.local.entities.StockEntity
import com.section11.mystock.models.Stock
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface StockRepository {
    fun getAllStocks(): Flow<List<Stock>>
}

class StockWatchListRoomRepository @Inject constructor(private val stockDao: StockDao) : StockRepository {
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
