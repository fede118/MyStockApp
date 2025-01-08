package com.section11.mystock.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StockWatchlistUseCase
import com.section11.mystock.models.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val stocks: List<Stock>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel@Inject constructor(
    private val stockWatchlistUseCase: StockWatchlistUseCase,
    private val dispatcher: CoroutineDispatcher
): ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getStocks() {
        viewModelScope.launch(dispatcher) {
            stockWatchlistUseCase.getWatchlist().collect { stocks ->
                _uiState.update { HomeUiState.Success(stocks) }
            }
        }
    }
}
