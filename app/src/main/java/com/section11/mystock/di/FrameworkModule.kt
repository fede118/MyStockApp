package com.section11.mystock.di

import android.content.Context
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.framework.featureflags.LocalFeatureFlagManager
import com.section11.mystock.framework.resource.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

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
}
