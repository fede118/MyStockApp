package com.section11.mystock.ui.singlestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.watchlist.StockWatchlistUseCase
import com.section11.mystock.ui.common.events.UiEvent
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Error
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.model.ActionableIconUiModel
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AddToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AlreadyAddedToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.LoadingIcon
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.AddToWatchlistTap
import com.section11.mystock.ui.singlestock.events.SingleStockScreenEvent.RemoveFromWatchlistTap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INVALID_SYMBOL_ERROR = "Invalid symbol"

@HiltViewModel
class SingleStockViewModel @Inject constructor(
    private val stocksInformationUseCase: StocksInformationUseCase,
    private val stockInfoUiMapper: StockInformationUiModelMapper,
    private val stockWatchlistUseCase: StockWatchlistUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _actionableIconState = MutableStateFlow<ActionableIconState>(LoadingIcon)
    val actionableIconState: StateFlow<ActionableIconState> = _actionableIconState.asStateFlow()

    fun getStockInformation(symbol: String?, exchange: String? = null) {
        if (symbol == null) {
            _uiState.value = Error(INVALID_SYMBOL_ERROR)
            return
        }

        _uiState.value = Loading
        viewModelScope.launch(dispatcher) {
            try {
                val stockResult = stocksInformationUseCase.getStockInformation(symbol, exchange)
                val isStockInWatchlist = stockWatchlistUseCase.isStockInWatchlist(stockResult.summary.symbol)
                val stockUiModel = stockInfoUiMapper.mapToUiModel(stockResult)
                _uiState.value = SingleStockFetched(stockUiModel)
                _actionableIconState.value = stockInfoUiMapper.getActionableIconUiModel(
                    stockResult.summary,
                    isStockInWatchlist
                )

            } catch (nullBodyException: ResponseBodyNullException) {
                _uiState.value = Error(nullBodyException.message)
            } catch (apiErrorException: ApiErrorException) {
                _uiState.value = Error(apiErrorException.message)
            }
        }
    }

    fun onSingleStockScreenEvent(event: UiEvent) {
        // Events from other flows shouldn't reach this ViewModel
        if (event !is SingleStockScreenEvent) return // Events from other flows shouldn't reach this ViewModel
        _actionableIconState.value = LoadingIcon
        viewModelScope.launch(dispatcher) {
            when(event) {
                is AddToWatchlistTap -> {
                    with(event.iconUiModel) {
                        stockWatchlistUseCase.saveStockToWatchlist(Stock(title, symbol, exchange))
                        val iconModel = stockInfoUiMapper.updateIconOnUiModel(this)
                        _actionableIconState.value = AlreadyAddedToWatchlist(iconModel)
                    }
                }
                is RemoveFromWatchlistTap -> {
                    with(event.iconUiModel) {
                        stockWatchlistUseCase.removeStockFromWatchlist(symbol)
                        val iconModel = stockInfoUiMapper.updateIconOnUiModel(this)
                        _actionableIconState.value = AddToWatchlist(iconModel)
                    }
                }
            }
        }
    }

    sealed class SingleStockUiState : UiState {
        data class SingleStockFetched(
            val stockInformationUiModel: StockInformationUiModel
        ) : SingleStockUiState()
    }

    sealed class ActionableIconState {
        data object LoadingIcon : ActionableIconState()
        data class AddToWatchlist(val iconUiModel: ActionableIconUiModel): ActionableIconState()
        data class AlreadyAddedToWatchlist(val iconUiModel: ActionableIconUiModel): ActionableIconState()
    }
}
