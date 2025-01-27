package com.section11.mystock.ui.home.events

import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.model.WatchlistStockModel

sealed class WatchlistScreenEvent {
    data class StockTapped(val stock: WatchlistStockModel) : WatchlistScreenEvent()
    data class SearchPerformed(val query: String) : WatchlistScreenEvent()
    data class SearchResultTapped(val result: StockSearchResultUiModel) : WatchlistScreenEvent()
    data object SearchBarClosed : WatchlistScreenEvent()
    data object ShakeDetected : WatchlistScreenEvent()
}
