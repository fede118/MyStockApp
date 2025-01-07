package com.section11.mystock.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.section11.mystock.data.local.daos.StockDao
import com.section11.mystock.data.local.entities.StockEntity

@Database(entities = [StockEntity::class], version = 1, exportSchema = false)
abstract class MyStockDatabase : RoomDatabase() {
    abstract fun stockDao(): StockDao
}