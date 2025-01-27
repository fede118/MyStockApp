package com.section11.mystock.di

import com.section11.mystock.framework.environment.EnvironmentManager
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Prod
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Test
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val DEFAULT_TIME_OUT = 15L
private const val WRITE_TIME_OUT = 45L


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun getCurrentEnvironmentRetrofit(
        environmentManager: EnvironmentManager,
        @Named(PROD_RETROFIT) prodRetrofit: Retrofit,
        @Named(TEST_RETROFIT) testRetrofit: Retrofit
    ): Retrofit {
        return if (environmentManager.currentEnvironment == Prod) {
            prodRetrofit
        } else {
            testRetrofit
        }
    }

    @Provides
    @Singleton
    @Named(PROD_RETROFIT)
    fun provideProdRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Prod.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    @Provides
    @Singleton
    @Named(TEST_RETROFIT)
    fun provideTestRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Test.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()
    }

    companion object {
        const val PROD_RETROFIT = "ProdRetrofit"
        const val TEST_RETROFIT = "TestRetrofit"
    }
}
