package com.section11.mystock.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.HOME_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_ROUTE
import com.section11.mystock.ui.navigation.MyStockNavigationActions.Companion.SINGLE_STOCK_SYMBOL
import com.section11.mystock.ui.home.composables.HomeRoute
import com.section11.mystock.ui.home.HomeViewModel

@Suppress("ForbiddenComment") // to do comment will be addressed soon
@Composable
fun MyStocksNavGraph(
    modifier: Modifier = Modifier,
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
        ) { navBackStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(navBackStackEntry)
            HomeRoute(homeViewModel, navigationActions.navigateToSingleStockView)
        }

        composable(
            route = SINGLE_STOCK_ROUTE,
            arguments = listOf(
                navArgument(SINGLE_STOCK_SYMBOL) { type = NavType.StringType }
            )
        ) { navBackStackEntry ->
            with(navBackStackEntry.arguments) {
                // TODO: next create Single Stock screen/route
                val symbol = this?.getString(SINGLE_STOCK_SYMBOL)
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (symbol != null) {
                        Text(text ="SYMBOL TAPPED = $symbol")
                    } else {
                        Text(text ="Error")
                    }
                }

            }
        }
    }
}
