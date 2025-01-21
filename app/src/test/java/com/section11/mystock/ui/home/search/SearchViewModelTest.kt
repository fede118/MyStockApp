package com.section11.mystock.ui.home.search

import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setup() {
        searchViewModel = SearchViewModel()
    }

    @Test
    fun `when search is performed then it should call search on use case`() {
        //todo this nneeds to change since for now we dont have a use case

        // When
        searchViewModel.onSearch("query")

        // Then
        assert(searchViewModel.uiState.value is SearchViewModel.SearchUiState.SearchResults)
    }

    @Test
    fun `when search bar is closed then the Ui state should be IDLE`() {
        // When
        searchViewModel.onSearchBarClosed()

        // Then
        assert(searchViewModel.uiState.value is SearchViewModel.SearchUiState.Idle)
    }

    @Test
    fun `when search result is tapped then should show snackbar`() {

        searchViewModel.onResultTapped("any result")

        assert(searchViewModel.uiState.value is SearchViewModel.SearchUiState.ShowSnackBar)
    }
}
