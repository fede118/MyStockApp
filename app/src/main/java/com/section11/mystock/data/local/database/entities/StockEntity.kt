package com.section11.mystock.data.local.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stocks_watchlist")
data class StockEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val symbol: String,
    val exchange: String
)
