package com.section11.mystock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.section11.mystock.di.NavigationManagerEntryPoint
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel.SecretMenuEvent.ChangeEnvironment
import com.section11.mystock.framework.secretmenu.composables.SecretMenuScreen
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.GetSingleStockInfo
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSecretMenu
import com.section11.mystock.ui.common.navigation.NavigationManager.NavigationEvent.ToSingleStock
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.MY_STOCKS_NAV_GRAPH_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SECRET_MENU_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_SYMBOL
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import dagger.hilt.android.EntryPointAccessors

@Composable
fun MyStocksNavGraph(
    modifier: Modifier = Modifier,
    onBottomBarChange: (@Composable () -> Unit) -> Unit,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HOME_ROUTE
) {
    val navActions = MyStockNavigationActions(navController)
    val appContext = LocalContext.current.applicationContext
    val navigationManager = remember {
        EntryPointAccessors.fromApplication(
            appContext,
            NavigationManagerEntryPoint::class.java
        ).navigationManager()
    }

    NavHost(
        route = MY_STOCKS_NAV_GRAPH_ROUTE,
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = HOME_ROUTE
        )  { navBackStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(navBackStackEntry)
            val searchViewModel = hiltViewModel<SearchViewModel>(navBackStackEntry)
            val singleStockViewModel = getSingleStockViewModelFromParentEntry(navController)
            HomeRoute(homeViewModel, searchViewModel, singleStockViewModel, onBottomBarChange)
        }

        composable(
            route = SINGLE_STOCK_ROUTE,
            arguments = listOf(
                navArgument(SINGLE_STOCK_SYMBOL) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            val singleStockViewModel = getSingleStockViewModelFromParentEntry(navController)
            with(navBackStackEntry.arguments) {
                val symbol = this?.getString(SINGLE_STOCK_SYMBOL)
                SingleStockViewRoute(symbol, singleStockViewModel)
            }
        }

        composable(route = SECRET_MENU_ROUTE) {
            val viewModel: SecretMenuViewModel = hiltViewModel()
            val context = LocalContext.current
            SecretMenuScreen(viewModel.secretMenuState) { event ->
                when(event) {
                    is ChangeEnvironment -> {
                        viewModel.onChangeEnvironmentTapped(event.environment)
                        navActions.restartActivity(context)
                    }
                }
            }
        }
    }

    val singleStockViewModel = getSingleStockViewModelFromParentEntry(navController)
    LaunchedEffect(navigationManager.navigationEvent) {
        navigationManager.navigationEvent.collect { event ->
            when (event) {
                is ToSingleStock -> navActions.navigateToSingleStockView(event.symbol)
                is GetSingleStockInfo -> singleStockViewModel.getStockInformation(event.symbol)
                is ToSecretMenu -> navActions.navigateToSecretMenu()
            }
        }
    }
}

@Composable
fun getSingleStockViewModelFromParentEntry(navController: NavController): SingleStockViewModel {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(MY_STOCKS_NAV_GRAPH_ROUTE)
    }
    return hiltViewModel(parentEntry)
}
