package com.section11.mystock.ui.singlestock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.domain.exceptions.ResponseBodyNullException
import com.section11.mystock.domain.models.StockInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleStockViewModel @Inject constructor(
    private val stocksInformationUseCase: StocksInformationUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {

    private val _uiState = MutableStateFlow<SingleStockUiState>(SingleStockUiState.Loading)
    val uiState: StateFlow<SingleStockUiState> = _uiState.asStateFlow()

    fun getStockInformation(symbol: String) {
        viewModelScope.launch(dispatcher) {
            try {
                val stockResult = stocksInformationUseCase.getStockInformation(symbol)
                _uiState.update { SingleStockUiState.Success(stockResult) }
            } catch (nullBodyException: ResponseBodyNullException) {
                _uiState.update {SingleStockUiState.Error(nullBodyException.message) }
            } catch (apiErrorException: ApiErrorException) {
                _uiState.update { SingleStockUiState.Error(apiErrorException.message) }
            }
        }

    }
    sealed class SingleStockUiState {
        data object Loading : SingleStockUiState()
        data class Success(val stockInformation: StockInformation) : SingleStockUiState()
        data class Error(val message: String?) : SingleStockUiState()
    }
}
