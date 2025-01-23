package com.section11.mystock.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_SYMBOL
import com.section11.mystock.ui.singlestock.SingleStockViewModel

@Composable
fun MyStocksNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState,
    startDestination: String = HOME_ROUTE
) {
    val navigationActions = MyStockNavigationActions(navController)
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            route = HOME_ROUTE
        )  { navBackStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(navBackStackEntry)
            val searchViewModel = hiltViewModel<SearchViewModel>(navBackStackEntry)
            val singleStockViewModel = hiltViewModel<SingleStockViewModel>(navBackStackEntry)
            HomeRoute(
                homeViewModel,
                searchViewModel,
                singleStockViewModel,
                snackbarHostState
            ) { symbol ->
                navigationActions.navigateToSingleStockView(symbol)
            }
        }

        composable(
            route = SINGLE_STOCK_ROUTE,
            arguments = listOf(
                navArgument(SINGLE_STOCK_SYMBOL) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            with(navBackStackEntry.arguments) {
                val symbol = this?.getString(SINGLE_STOCK_SYMBOL)
                val singleStockViewModel = hiltViewModel<SingleStockViewModel>(navBackStackEntry)
                SingleStockViewRoute(symbol, singleStockViewModel)
            }
        }
    }
}
