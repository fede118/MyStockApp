package com.section11.mystock.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Error
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Loading
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.Success
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.home.composables.WatchlistScreen
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchBarClosed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchPerformed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchResultTapped
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.StockTapped
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.home.search.SearchViewModel.SearchUiState.ShowSnackBar
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    snackbarHostState: SnackbarHostState,
    navigateToSingleStock: (String) -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val searchUiState by searchViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        homeViewModel.getStocks()
    }

    LaunchedEffect(homeViewModel.navigationEvent) {
        homeViewModel.navigationEvent.collect { event ->
            when (event) {
                is ToSingleStock -> navigateToSingleStock(event.symbol)
            }
        }
    }

    LaunchedEffect(searchUiState) {
        when(searchUiState) {
            is ShowSnackBar -> scope.launch {
                val message = (searchUiState as ShowSnackBar).message
                snackbarHostState.showSnackbar(message)
            }
            else -> Unit
        }
    }

    HomeScreenContent(
        homeUiState = homeUiState,
        searchUiStateFlow = searchViewModel.uiState,
        homeViewModel = homeViewModel,
        onEvent = { event ->
            when (event) {
                is StockTapped -> homeViewModel.onStockTap(event.stock.symbol)
                is SearchPerformed -> searchViewModel.onSearch(event.query)
                is SearchResultTapped -> searchViewModel.onResultTapped(event.result)
                is SearchBarClosed -> searchViewModel.onSearchBarClosed()
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    homeUiState: HomeViewModel.HomeUiState,
    searchUiStateFlow: StateFlow<SearchViewModel.SearchUiState>,
    homeViewModel: HomeViewModel,
    onEvent: (WatchlistScreenEvent) -> Unit
) {
    when (homeUiState) {
        is Loading -> FullScreenLoading()
        is Success -> WatchlistScreen(
            stocksScreenUiModel = homeUiState.stocksScreenUiModel,
            onEvent = onEvent,
            singleStockInfoState = homeViewModel.singleStockInformationState,
            searchUiStateFlow = searchUiStateFlow
        )
        is Error -> Text("Error: ${homeUiState.message}")
    }
}


@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}
