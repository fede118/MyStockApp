package com.section11.mystock.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.section11.mystock.domain.StocksInformationUseCase
import com.section11.mystock.domain.exceptions.ApiErrorException
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.model.mapper.StockSearchResultUiMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val stocksInformationUseCase: StocksInformationUseCase,
    private val stockSearchResultUiMapper: StockSearchResultUiMapper,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onSearch(query: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(dispatcher) {
            try {
                val searchResult = stocksInformationUseCase.searchStock(query)
                val stockUiModel = stockSearchResultUiMapper.mapToUiModel(searchResult)
                _uiState.value = SearchBarUiState.SearchResults(stockUiModel)
            } catch (apiErrorException: ApiErrorException) {
                _uiState.value = UiState.Error(apiErrorException.message)
            }
        }
    }

    fun onResultTapped(result: String) {
        _uiState.value = SearchBarUiState.ShowSnackBar("Tapped: $result")
        // todo navigate to single stock screen and or add them to watchlist
    }

    fun onSearchBarClosed() {
        _uiState.value = UiState.Idle
    }

    sealed class SearchBarUiState : UiState {
        data class ShowSnackBar(val message: String) : SearchBarUiState()
        data class SearchResults(val results: List<StockSearchResultUiModel>) : SearchBarUiState()
    }
}
