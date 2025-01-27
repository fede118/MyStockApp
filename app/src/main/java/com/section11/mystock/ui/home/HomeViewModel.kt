package com.section11.mystock.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.common.Const.COLON
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.ui.common.navigation.NavigationManager
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.GetSingleStockInfo
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSecretMenu
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.WatchlistFetched
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stockWatchlistUseCase: StockWatchlistUseCase,
    private val stockWatchlistUiModelMapper: StockWatchlistUiModelMapper,
    private val featureFlagManager: FeatureFlagManager,
    private val navigationManager: NavigationManager,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

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

    fun onStockTap(stockModel: WatchlistStockModel) {
        viewModelScope.launch(dispatcher) {
            if (featureFlagManager.isNavigationToSingleStockEnabled()) {
                navigationManager.navigate(
                    ToSingleStock(stockModel.symbol + COLON + stockModel.exchange)
                )
            } else {
                navigationManager.navigate(GetSingleStockInfo(stockModel.symbol))
            }
        }
    }

    fun onShakeDetected() {
        viewModelScope.launch(dispatcher) {
            navigationManager.navigate(ToSecretMenu)
        }
    }

    sealed class HomeUiState : UiState {
        data class WatchlistFetched(val stocksScreenUiModel: WatchlistScreenUiModel): HomeUiState()
    }
}
