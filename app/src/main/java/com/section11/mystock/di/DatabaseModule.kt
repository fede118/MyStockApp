package com.section11.mystock.di

import android.content.Context
import androidx.room.Room
import com.section11.mystock.data.local.database.MyStockDatabase
import com.section11.mystock.data.local.database.daos.StockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MyStockDatabase {
        return Room.databaseBuilder(
            context,
            MyStockDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideStockDao(appDatabase: MyStockDatabase): StockDao {
        return appDatabase.stockDao()
    }
}
