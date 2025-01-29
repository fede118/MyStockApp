package com.section11.mystock.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.section11.mystock.di.NavigationManagerEntryPoint
import com.section11.mystock.ui.common.composables.MyStockLoader
import com.section11.mystock.ui.common.composables.SecretMenuShakeDetector
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.GetSingleStockInfo
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSecretMenu
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.common.uistate.UiState
import com.section11.mystock.ui.common.uistate.UiState.Error
import com.section11.mystock.ui.common.uistate.UiState.Loading
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.HomeViewModel.HomeUiState.WatchlistFetched
import com.section11.mystock.ui.home.composables.WatchlistScreen
import com.section11.mystock.ui.home.events.WatchlistScreenEvent
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchBarClosed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchPerformed
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.SearchResultTapped
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.ShakeDetected
import com.section11.mystock.ui.home.events.WatchlistScreenEvent.StockTapped
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import com.section11.mystock.ui.theme.LocalDimens
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    singleStockViewModel: SingleStockViewModel,
    navigateToSingleStock: (String) -> Unit,
    navigateToSecretMenu: () -> Unit,
    onBottomBarChange: (@Composable () -> Unit) -> Unit
) {
    val homeUiState by homeViewModel.uiState.collectAsState()
    val appContext = LocalContext.current.applicationContext
    val navigationManager = remember {
        EntryPointAccessors.fromApplication(
            appContext,
            NavigationManagerEntryPoint::class.java
        ).navigationManager()
    }

    LaunchedEffect(navigationManager.navigationEvent) {
        navigationManager.navigationEvent.collect { event ->
            when (event) {
                is ToSingleStock -> navigateToSingleStock(event.symbolColonExchange)
                is GetSingleStockInfo -> singleStockViewModel.getStockInformation(event.symbol)
                is ToSecretMenu -> navigateToSecretMenu()
            }
        }
    }

    HomeScreenContent(
        homeUiState = homeUiState,
        searchUiStateFlow = searchViewModel.uiState,
        singleStockInfoState = singleStockViewModel.uiState,
        onBottomBarChange = onBottomBarChange,
        onEvent = { event ->
            when (event) {
                is StockTapped -> homeViewModel.onStockTap(event.stock)
                is SearchPerformed -> searchViewModel.onSearch(event.query)
                is SearchResultTapped -> searchViewModel.onResultTapped(event.result)
                is SearchBarClosed -> searchViewModel.onSearchBarClosed()
                is ShakeDetected -> homeViewModel.onShakeDetected()
            }
        }
    )
}

@Composable
private fun HomeScreenContent(
    homeUiState: UiState,
    searchUiStateFlow: StateFlow<UiState>,
    singleStockInfoState: StateFlow<UiState>,
    onEvent: (WatchlistScreenEvent) -> Unit,
    onBottomBarChange: (@Composable () -> Unit) -> Unit
) {
    when (homeUiState) {
        is Loading -> MyStockLoader()
        is WatchlistFetched -> WatchlistScreen(
            stocksScreenUiModel = homeUiState.stocksScreenUiModel,
            onEvent = onEvent,
            singleStockInfoState = singleStockInfoState,
            searchUiStateFlow = searchUiStateFlow
        ).also {
            onBottomBarChange.invoke {
                BottomBarAppVersionInfo(homeUiState.stocksScreenUiModel.appVersionInfo) { event ->
                    onEvent(event)
                }
            }
        }

        is Error -> Text("Error: ${homeUiState.message}")
    }
}

@Composable
fun BottomBarAppVersionInfo(appVersion: String, onEvent: (WatchlistScreenEvent) -> Unit) {
    val dimens = LocalDimens.current
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimens.m6)
            .background(Color.Transparent)
    ) {
        Text(
            fontSize = dimens.textSmallest,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = dimens.m2),
            text = appVersion
        )
    }
    SecretMenuShakeDetector {
        onEvent(ShakeDetected)
    }
}
