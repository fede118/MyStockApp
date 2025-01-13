package com.section11.mystock.di

import android.content.Context
import androidx.room.Room
import com.section11.mystock.BuildConfig
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.data.local.database.MyStockDatabase
import com.section11.mystock.data.local.database.daos.StockDao
import com.section11.mystock.data.local.database.entities.StockEntity
import com.section11.mystock.data.repositories.RoomStockWatchlistRepository
import com.section11.mystock.data.repositories.StocksInformationRemoteRepository
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import com.section11.mystock.domain.repositories.StocksInformationRepository
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.framework.resource.ResourceProviderImpl
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val DEFAULT_TIME_OUT = 15L
private const val WRITE_TIME_OUT = 45L

@Module
@InstallIn(SingletonComponent::class)
abstract class MyStockModule {

    @Binds
    abstract fun bindStockRepository(stockRepositoryImpl: RoomStockWatchlistRepository): StockWatchlistRepository

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
        fun provideStockWatchlistUseCase(stockWatchlistRepository: StockWatchlistRepository): StockWatchlistUseCase {
            return StockWatchlistUseCase(stockWatchlistRepository)
        }

        @Provides
        fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
            return ResourceProviderImpl(context)
        }

        @Provides
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        fun provideStockInformationRemoteRepository(): StocksInformationRepository {
            return StocksInformationRemoteRepository(
                provideStockInformationService(), BuildConfig.SERP_API_KEY, BuildConfig.SERP_API_DEFAULT_MARKET
            )
        }

        private fun provideStockInformationService(): StocksInformationService {
            return getSerpApiRetrofitInstance().create(StocksInformationService::class.java)
        }

        @Singleton
        private fun getSerpApiRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.SERP_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(provideOkHttpClient())
                .build()
        }

        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .build()
        }
    }
}
