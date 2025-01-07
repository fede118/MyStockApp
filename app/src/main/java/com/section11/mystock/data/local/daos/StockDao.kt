package com.section11.mystock.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.section11.mystock.data.local.entities.StockEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(stock: StockEntity)

    @Query("SELECT * FROM stocks_watchlist")
    fun getStocksWatchlist(): Flow<List<StockEntity>>
}
