package com.section11.mystock.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel
import com.section11.mystock.framework.secretmenu.SecretMenuViewModel.SecretMenuEvent.ChangeEnvironment
import com.section11.mystock.framework.secretmenu.composables.SecretMenuScreen
import com.section11.mystock.ui.home.HomeViewModel
import com.section11.mystock.ui.home.search.SearchViewModel
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SECRET_MENU_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_SYMBOL
import com.section11.mystock.ui.singlestock.SingleStockViewModel

@Composable
fun MyStocksNavGraph(
    modifier: Modifier = Modifier,
    onBottomBarChange: (@Composable () -> Unit) -> Unit,
    navController: NavHostController = rememberNavController(),
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
                homeViewModel = homeViewModel,
                searchViewModel = searchViewModel,
                singleStockViewModel = singleStockViewModel,
                navigateToSingleStock = { symbol ->
                    navigationActions.navigateToSingleStockView(symbol)
                },
                navigateToSecretMenu = {
                    navigationActions.navigateToSecretMenu()
                },
                onBottomBarChange = onBottomBarChange
            )
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

        composable(route = SECRET_MENU_ROUTE) {
            val viewModel: SecretMenuViewModel = hiltViewModel()
            val context = LocalContext.current
            SecretMenuScreen(viewModel.secretMenuState) { event ->
                when(event) {
                    is ChangeEnvironment -> {
                        viewModel.onChangeEnvironmentTapped(event.environment)
                        navigationActions.restartActivity(context)
                    }
                }
            }
        }
    }
}
