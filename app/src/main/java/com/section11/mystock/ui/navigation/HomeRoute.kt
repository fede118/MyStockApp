package com.section11.mystock.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.section11.mystock.BuildConfig
import com.section11.mystock.framework.secretmenu.ShakeDetector
import com.section11.mystock.ui.common.composables.MyStockLoader
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Error
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.WatchlistFetched
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.GetSingleStockInfo
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.ToSecretMenu
import com.section11.mystock.ui.home.HomeViewModel.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.home.composables.WatchlistScreen
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchBarClosed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchPerformed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchResultTapped
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.StockTapped
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.home.search.SearchViewModel.SearchBarUiState.ShowSnackBar
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    singleStockViewModel: SingleStockViewModel,
    snackbarHostState: SnackbarHostState,
    navigateToSingleStock: (String) -> Unit,
    navigateToSecretMenu: () -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val searchUiState by searchViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(homeViewModel.navigationEvent) {
        homeViewModel.navigationEvent.collect { event ->
            when (event) {
                is ToSingleStock -> navigateToSingleStock(event.symbol)
                is GetSingleStockInfo -> singleStockViewModel.getStockInformation(event.symbol)
                is ToSecretMenu -> navigateToSecretMenu()
            }
        }
    }

    LaunchedEffect(searchUiState) {
        when(searchUiState) {
            is ShowSnackBar -> {
                scope.launch {
                    val message = (searchUiState as ShowSnackBar).message
                    snackbarHostState.showSnackbar(message)
                }
            }
            else -> Unit
        }
    }

    if(BuildConfig.DEBUG) {
        SecretMenuShakeDetector {
            homeViewModel.onShakeDetected()
        }
    }
    HomeScreenContent(
        homeUiState = homeUiState,
        searchUiStateFlow = searchViewModel.uiState,
        singleStockInfoState = singleStockViewModel.uiState,
        onEvent = { event ->
            when (event) {
                is StockTapped -> homeViewModel.onStockTap(event.stock.symbol)
                is SearchPerformed -> searchViewModel.onSearch(event.query)
                is SearchResultTapped -> searchViewModel.onResultTapped(event.result.symbol)
                is SearchBarClosed -> searchViewModel.onSearchBarClosed()
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    homeUiState: UiState,
    searchUiStateFlow: StateFlow<UiState>,
    singleStockInfoState: StateFlow<UiState>,
    onEvent: (WatchlistScreenEvent) -> Unit
) {
    when (homeUiState) {
        is Loading -> MyStockLoader()
        is WatchlistFetched -> WatchlistScreen(
            stocksScreenUiModel = homeUiState.stocksScreenUiModel,
            onEvent = onEvent,
            singleStockInfoState = singleStockInfoState,
            searchUiStateFlow = searchUiStateFlow
        )
        is Error -> Text("Error: ${homeUiState.message}")
    }
}

@Composable
private fun SecretMenuShakeDetector(
    onShakeDetected: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val shakeDetector = ShakeDetector(context = context) {
            onShakeDetected()
        }

        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> shakeDetector.start()
                Lifecycle.Event.ON_PAUSE -> shakeDetector.stop()
                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            shakeDetector.stop()
        }
    }
}
