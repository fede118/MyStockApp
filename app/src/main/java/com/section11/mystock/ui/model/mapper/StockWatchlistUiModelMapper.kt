package com.section11.mystock.ui.model.mapper

import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.ui.model.WatchlistStockModel
import javax.inject.Inject

class StockWatchlistUiModelMapper@Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun mapToUiModel(stocks: List<Stock>): List<WatchlistStockModel> {
        return stocks.map { stock ->
            WatchlistStockModel(
                stockTitle = getStockTitle(stock),
                symbol = stock.symbol
            )
        }
    }

    private fun getStockTitle(stock: Stock): String {
        return resourceProvider.getString(R.string.watchlist_stock_tile, stock.name, stock.symbol)
    }
}
