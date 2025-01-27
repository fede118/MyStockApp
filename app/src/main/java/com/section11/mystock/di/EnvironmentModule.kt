package com.section11.mystock.di

import android.content.SharedPreferences
import com.section11.mystock.framework.environment.EnvironmentManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class EnvironmentModule {

    @Provides
    @Singleton
    fun provideEnvironmentManager(sharedPreferences: SharedPreferences): EnvironmentManager {
        return EnvironmentManager(sharedPreferences)
    }
}
