package com.section11.mystock.ui.singlestock.events

import com.section11.mystock.ui.common.events.UiEvent
import com.section11.mystock.ui.model.ActionableIconUiModel

sealed class SingleStockScreenEvent : UiEvent {
    data class AddToWatchlistTap(val iconUiModel: ActionableIconUiModel): SingleStockScreenEvent()
    data class RemoveFromWatchlistTap(val iconUiModel: ActionableIconUiModel): SingleStockScreenEvent()
}
