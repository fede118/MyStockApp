package com.section11.mystock.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Error
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.WatchlistFetched
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INVALID_SYMBOL_ERROR = "Invalid symbol"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stockWatchlistUseCase: StockWatchlistUseCase,
    private val stockWatchlistUiModelMapper: StockWatchlistUiModelMapper,
    private val featureFlagManager: FeatureFlagManager,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent

    init {
        getStocks()
    }

    private fun getStocks() {
        viewModelScope.launch(dispatcher) {
            stockWatchlistUseCase.getWatchlist().collect { stocks ->
                _uiState.value =
                    WatchlistFetched(stockWatchlistUiModelMapper.mapToUiModel(stocks))
            }
        }
    }

    fun onStockTap(symbol: String?) {
        if (symbol == null) {
            _uiState.value = Error(INVALID_SYMBOL_ERROR)
            return
        } else
            viewModelScope.launch(dispatcher) {
                if (featureFlagManager.isNavigationToSingleStockEnabled()) {
                    _navigationEvent.emit(NavigationEvent.ToSingleStock(symbol))
                } else {
                    _navigationEvent.emit(NavigationEvent.GetSingleStockInfo(symbol))
                }
            }
    }

    fun onShakeDetected() {
        viewModelScope.launch(dispatcher) {
            _navigationEvent.emit(NavigationEvent.ToSecretMenu)
        }
    }

    sealed class HomeUiState : UiState {
        data class WatchlistFetched(val stocksScreenUiModel: WatchlistScreenUiModel): HomeUiState()
    }

    sealed class NavigationEvent {
        data class ToSingleStock(val symbol: String) : NavigationEvent()
        data class GetSingleStockInfo(val symbol: String) : NavigationEvent()
        data object ToSecretMenu: NavigationEvent()
    }
}
