package com.section11.mystock.data.local.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.section11.mystock.data.local.database.entities.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stock: StockEntity)

    @Query("SELECT * FROM stocks_watchlist")
    fun getStocksWatchlist(): Flow<List<StockEntity>>

    @Query("SELECT * FROM stocks_watchlist WHERE symbol = :symbol")
    fun getStockBySymbol(symbol: String): StockEntity?

    @Query("DELETE FROM stocks_watchlist WHERE symbol = :symbol")
    suspend fun remove(symbol: String)
}
