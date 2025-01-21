package com.section11.mystock.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.framework.featureflags.FeatureFlagManager
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Error
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Success
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.ErrorFetchingSingleStockInfo
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.FetchedSingleStockInfo
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INVALID_SYMBOL_ERROR = "Invalid symbol"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val stockWatchlistUseCase: StockWatchlistUseCase,
    private val stockWatchlistUiModelMapper: StockWatchlistUiModelMapper,
    private val stocksInformationUseCase: StocksInformationUseCase,
    private val stockInformationUiModelMapper: StockInformationUiModelMapper,
    private val featureFlagManager: FeatureFlagManager,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent

    private val _singleStockInformationState = MutableStateFlow<SingleStockInformationState>(
        SingleStockInformationState.Idle
    )
    val singleStockInformationState: StateFlow<SingleStockInformationState> =
        _singleStockInformationState.asStateFlow()

    fun getStocks() {
        viewModelScope.launch(dispatcher) {
            stockWatchlistUseCase.getWatchlist().collect { stocks ->
                _uiState.update {
                    Success(stockWatchlistUiModelMapper.mapToUiModel(stocks))
                }
            }
        }
    }

    fun onStockTap(symbol: String?) {
        if (symbol == null) {
            _uiState.update { Error(INVALID_SYMBOL_ERROR) }
            return
        } else

            viewModelScope.launch(dispatcher) {
                if (featureFlagManager.isNavigationToSingleStockEnabled()) {
                    _navigationEvent.emit(NavigationEvent.ToSingleStock(symbol))
                } else {
                    _singleStockInformationState.emit(SingleStockInformationState.Loading)
                    getSingleStockInformation(symbol)
                }
            }
    }

    private suspend fun getSingleStockInformation(symbol: String) {
        try {
            val stockResult = stocksInformationUseCase.getStockInformation(symbol)
            val stockUiModel = stockInformationUiModelMapper.mapToUiModel(stockResult)
            _singleStockInformationState.emit(FetchedSingleStockInfo(stockUiModel))
        } catch (nullBodyException: ResponseBodyNullException) {
            _singleStockInformationState.update {
                ErrorFetchingSingleStockInfo(nullBodyException.message)
            }
        } catch (apiErrorException: ApiErrorException) {
            _singleStockInformationState.update {
                ErrorFetchingSingleStockInfo(apiErrorException.message)
            }
        }
    }

    sealed class HomeUiState {
        data object Loading: HomeUiState()
        data class Error(val message: String?): HomeUiState()
        data class Success(val stocksScreenUiModel: WatchlistScreenUiModel): HomeUiState()
    }

    sealed class SingleStockInformationState {
        data object Loading: SingleStockInformationState()
        data class FetchedSingleStockInfo(
            val stockInfo: StockInformationUiModel
        ): SingleStockInformationState()
        data class ErrorFetchingSingleStockInfo(val message: String?): SingleStockInformationState()
        data object Idle: SingleStockInformationState()
    }

    sealed class NavigationEvent {
        data class ToSingleStock(val symbol: String) : NavigationEvent()
    }
}
