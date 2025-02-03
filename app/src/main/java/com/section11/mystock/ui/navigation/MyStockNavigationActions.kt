package com.section11.mystock.ui.navigation

import android.app.Activity
import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * Models the navigation of the app
 */
class MyStockNavigationActions(private val navController: NavController) {

    /**
     * Navigates to the Single Stock viewing screen
     * Pops up to the start destination of the graph to avoid building up a large stack
     * of destinations on the back stack as users select items
     *
     * sets launchSingleTop to true to Avoid multiple copies of the same destination when re-selecting
     * the same item
     * sets restoreState to true to restore state when re-selecting a previously selected item
     */
    fun navigateToSingleStockView(symbol: String) {
        val route = SINGLE_STOCK_ROUTE.replace("{$SINGLE_STOCK_SYMBOL}", symbol)
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToSecretMenu() = navController.navigate(SECRET_MENU_ROUTE)

    /**
     * This is only used for the secret menu. When we change the environment we need to restart
     * the activity so that hilt recreates Retrofit with the new base_url
     */
    fun restartActivity(context: Context) {
        val intent = (context as? Activity)?.intent
        intent?.let {
            with(context) {
                finish()
                startActivity(it)
            }
        }
    }

    companion object {
        const val HOME_ROUTE = "home"
        const val SINGLE_STOCK_SYMBOL = "symbol"
        const val SINGLE_STOCK_ROUTE = "single_stock/{$SINGLE_STOCK_SYMBOL}"
        const val SECRET_MENU_ROUTE = "secret_menu"
        const val MY_STOCKS_NAV_GRAPH_ROUTE = "my_stocks_nav_graph"
    }
}
