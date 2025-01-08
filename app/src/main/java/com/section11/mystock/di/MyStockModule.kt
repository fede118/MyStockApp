package com.section11.mystock.di

import android.content.Context
import androidx.room.Room
import com.section11.mystock.data.local.database.MyStockDatabase
import com.section11.mystock.data.local.database.daos.StockDao
import com.section11.mystock.data.local.database.entities.StockEntity
import com.section11.mystock.data.repositories.StockRepository
import com.section11.mystock.data.repositories.StockWatchListRoomRepository
import com.section11.mystock.domain.StockWatchlistUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyStockModule {

    @Binds
    abstract fun bindStockRepository(stockRepositoryImpl: StockWatchListRoomRepository): StockRepository

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): MyStockDatabase {
            return Room.databaseBuilder(
                context,
                MyStockDatabase::class.java,
                "app_database"
            ).build().apply {
                // TODO remove this, when search functionality is done
                stockDao().run {
                    CoroutineScope(Dispatchers.IO).launch {
                        getStocksWatchlist().collect { result ->
                            if(result.isEmpty()) {
                                insert(StockEntity(name = "Apple Inc", symbol = "AAPL"))
                                insert(StockEntity(name = "Tesla Inc", symbol = "TSLA"))
                                insert(StockEntity(name = "Microsoft Corp", symbol = "MSFT"))
                            }
                        }
                    }
                }
            }
        }

        @Provides
        fun provideStockDao(appDatabase: MyStockDatabase): StockDao {
            return appDatabase.stockDao()
        }

        @Provides
        @Singleton
        fun provideGetAllStocksUseCase(stockRepository: StockRepository): StockWatchlistUseCase {
            return StockWatchlistUseCase(stockRepository)
        }

        @Provides
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}
