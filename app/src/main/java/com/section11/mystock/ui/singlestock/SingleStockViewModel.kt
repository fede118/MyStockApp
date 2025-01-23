package com.section11.mystock.ui.singlestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Error
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
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
    private val stockInformationUiModelMapper: StockInformationUiModelMapper,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getStockInformation(symbol: String?) {
        if (symbol == null) {
            _uiState.value = Error(INVALID_SYMBOL_ERROR)
            return
        }

        _uiState.value = Loading
        viewModelScope.launch(dispatcher) {
            try {
                val stockResult = stocksInformationUseCase.getStockInformation(symbol)
                val stockUiModel = stockInformationUiModelMapper.mapToUiModel(stockResult)
                _uiState.value = SingleStockFetched(stockUiModel)
            } catch (nullBodyException: ResponseBodyNullException) {
                _uiState.value = Error(nullBodyException.message)
            } catch (apiErrorException: ApiErrorException) {
                _uiState.value = Error(apiErrorException.message)
            }
        }
    }

    sealed class SingleStockUiState : UiState {
        data class SingleStockFetched(
            val stockInformationUiModel: StockInformationUiModel
        ) : SingleStockUiState()
    }
}
