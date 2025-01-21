package com.section11.mystock.ui.home.search

import androidx.lifecycle.ViewModel
import com.section11.mystock.ui.home.search.SearchViewModel.SearchUiState.Idle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(Idle)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onSearch(query: String) {
        _uiState.value = SearchUiState.SearchResults(
            listOf("$query 1", "$query 2", "$query 3")
        )

        // if no results then send 1 item with value "no results" or something
    }

    fun onResultTapped(result: String) {
        _uiState.value = SearchUiState.ShowSnackBar("Tapped: $result")
        // todo navigate to single stock screen and or add them to watchlist
    }

    fun onSearchBarClosed() {
        _uiState.value = Idle
    }

    sealed class SearchUiState {
        data object Idle : SearchUiState()
        data class ShowSnackBar(val message: String): SearchUiState()
        data class SearchResults(val results: List<String>): SearchUiState()
    }
}
