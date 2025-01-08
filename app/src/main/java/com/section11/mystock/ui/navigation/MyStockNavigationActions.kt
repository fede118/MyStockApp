package com.section11.mystock.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.section11.mystock.models.Stock

/**
 * Models the navigation of the app
 */
class MyStockNavigationActions(navController: NavController) {

    /**
     * Navigates to the watchlist screen
     *  Pops up to the start destination of the graph to avoid building up a large stack
     *  of destinations on the back stack as users select items
     *
     *  sets launchSingleTop to true to Avoid multiple copies of the same destination when re-selecting
     *  the same item
     *  sets restoreState to true to restore state when re-selecting a previously selected item
     */
    val navigateToHome: () -> Unit = {
        navController.navigate(HOME_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    /**
     * Navigates to the Single Stock viewing screen
     *  Pops up to the start destination of the graph to avoid building up a large stack
     *  of destinations on the back stack as users select items
     *
     *  sets launchSingleTop to true to Avoid multiple copies of the same destination when re-selecting
     *  the same item
     *  sets restoreState to true to restore state when reselecting a previously selected item
     */
    val navigateToSingleStockView: (Stock) -> Unit = { stock ->
        navController.navigate("$SINGLE_STOCK_ROUTE${stock.symbol}") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    companion object {
        const val HOME_ROUTE = "home"
        const val SINGLE_STOCK_SYMBOL = "stock_symbol"
        const val SINGLE_STOCK_ROUTE = "single_stock/{$SINGLE_STOCK_SYMBOL}"
    }
}

