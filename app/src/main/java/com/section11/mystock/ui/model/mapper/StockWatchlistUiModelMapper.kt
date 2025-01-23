package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.ui.common.extentions.toPercentageFormat
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import javax.inject.Inject
import kotlin.random.Random

private const val MINUS_FIVE = -5.00
private const val PLUS_FIVE = 5.00
private const val ZERO_DOUBLE = 0.00

class StockWatchlistUiModelMapper@Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun mapToUiModel(stocks: List<Stock>): WatchlistScreenUiModel {
        return WatchlistScreenUiModel(
            searchHint = resourceProvider.getString(R.string.watchlist_search_hint),
            stocks = stocks.map { stock ->
                // TODO This should come from service, but cant find an API for multiple stocks
                val percentage = Random.nextDouble(MINUS_FIVE, PLUS_FIVE)
                val percentageColor = if (percentage < ZERO_DOUBLE ) Red else Green
                WatchlistStockModel(
                    stockTitle = getStockTitle(stock),
                    symbol = stock.symbol,
                    percentageChange = getStockPercentage(percentage),
                    percentageColor = percentageColor
                )
            }
        )
    }

    private fun getStockTitle(stock: Stock): String {
        return resourceProvider.getString(R.string.watchlist_stock_tile, stock.name, stock.symbol)
    }

    private fun getStockPercentage(percentage: Double): String {
        return resourceProvider.getString(
            R.string.with_percentage,
            percentage.toPercentageFormat()
        )
    }
}
