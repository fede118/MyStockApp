package com.section11.mystock.di

import android.content.SharedPreferences
import com.section11.mystock.framework.environment.EnvironmentManager
import com.section11.mystock.framework.environment.EnvironmentManager.Environment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EnvironmentModule {

    @Provides
    fun provideCurrentEnvironment(environmentManager: EnvironmentManager): Environment {
        return environmentManager.currentEnvironment
    }

    @Provides
    @Singleton
    fun provideEnvironmentManager(sharedPreferences: SharedPreferences): EnvironmentManager {
        return EnvironmentManager(sharedPreferences)
    }
}
