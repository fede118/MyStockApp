package com.section11.mystock.di

import android.content.Context
import com.section11.mystock.BuildConfig
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.common.Const.DEFAULT_EXCHANGE_MARKET
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.framework.featureflags.LocalFeatureFlagManager
import com.section11.mystock.framework.resource.ResourceProviderImpl
import com.section11.mystock.ui.common.navigation.NavigationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FrameworkModule {

    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProviderImpl(context)
    }

    @Provides
    fun provideFeatureFlagManager(): FeatureFlagManager {
        return LocalFeatureFlagManager()
    }

    @Provides
    @Singleton
    fun provideNavigationManager(): NavigationManager {
        return NavigationManager()
    }

    @Provides
    @Named(DEFAULT_EXCHANGE_MARKET)
    fun defaultExchangeMarket(): String {
        return BuildConfig.SERP_API_DEFAULT_MARKET
    }
}
