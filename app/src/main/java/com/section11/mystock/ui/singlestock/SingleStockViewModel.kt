package com.section11.mystock.ui.singlestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val INVALID_SYMBOL_ERROR = "Invalid symbol"

@HiltViewModel
class SingleStockViewModel @Inject constructor(
    private val stocksInformationUseCase: StocksInformationUseCase,
    private val stockInformationUiModelMapper: StockInformationUiModelMapper,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<SingleStockUiState>(SingleStockUiState.Loading)
    val uiState: StateFlow<SingleStockUiState> = _uiState.asStateFlow()

    fun getStockInformation(symbol: String?) {
        if (symbol == null) {
            _uiState.update { SingleStockUiState.Error(INVALID_SYMBOL_ERROR) }
            return
        }

        viewModelScope.launch(dispatcher) {
            try {
                val stockResult = stocksInformationUseCase.getStockInformation(symbol)
                val stockUiModel = stockInformationUiModelMapper.mapToUiModel(stockResult)
                _uiState.update { SingleStockUiState.Success(stockUiModel) }
            } catch (nullBodyException: ResponseBodyNullException) {
                _uiState.update {SingleStockUiState.Error(nullBodyException.message) }
            } catch (apiErrorException: ApiErrorException) {
                _uiState.update { SingleStockUiState.Error(apiErrorException.message) }
            }
        }
    }

    sealed class SingleStockUiState {
        data object Loading : SingleStockUiState()
        data class Success(val stockInformationUiModel: StockInformationUiModel) : SingleStockUiState()
        data class Error(val message: String?) : SingleStockUiState()
    }
}
