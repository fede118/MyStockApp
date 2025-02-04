package com.section11.mystock.di

import com.section11.mystock.BuildConfig
import com.section11.mystock.data.repositories.RoomStockWatchlistRepository
import com.section11.mystock.data.repositories.StocksInformationRemoteRepository
import com.section11.mystock.data.service.StocksInformationService
import com.section11.mystock.domain.repositories.StockWatchlistRepository
import com.section11.mystock.domain.repositories.StocksInformationRepository
import com.section11.mystock.framework.environment.EnvironmentManager
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Prod
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStockRepository(stockRepositoryImpl: RoomStockWatchlistRepository): StockWatchlistRepository

    companion object {

        @Provides
        fun provideStockInformationRemoteRepository(
            environmentManager: EnvironmentManager,
            retrofit: Retrofit
        ): StocksInformationRepository {
            val apiKey = if (environmentManager.currentEnvironment == Prod) {
                BuildConfig.SERP_API_KEY
            } else {
                String()
            }
            val service = retrofit.create(StocksInformationService::class.java)
            return StocksInformationRemoteRepository(
                service,
                apiKey
            )
        }
    }
}
