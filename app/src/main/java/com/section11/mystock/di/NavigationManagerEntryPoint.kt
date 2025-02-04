package com.section11.mystock.di

import com.section11.mystock.ui.common.navigation.NavigationManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NavigationManagerEntryPoint {
    fun navigationManager(): NavigationManager
}
