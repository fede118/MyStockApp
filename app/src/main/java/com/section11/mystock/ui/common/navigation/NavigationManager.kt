package com.section11.mystock.ui.common.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class NavigationManager {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent

    suspend fun navigate(event: NavigationEvent) {
        _navigationEvent.emit(event)
    }

    sealed class NavigationEvent {
        data class ToSingleStock(val symbolColonExchange: String) : NavigationEvent()
        data class GetSingleStockInfo(val symbol: String) : NavigationEvent()
        data object ToSecretMenu: NavigationEvent()
    }
}
