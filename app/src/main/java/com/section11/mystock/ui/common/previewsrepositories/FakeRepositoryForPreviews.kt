package com.section11.mystock.ui.common.previewsrepositories

import android.content.Context
import com.section11.mystock.domain.models.CloseMatchStock
import com.section11.mystock.domain.models.CloseMatchStockPriceMovement
import com.section11.mystock.domain.models.GraphInformation
import com.section11.mystock.domain.models.GraphNode
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.domain.models.Summary
import com.section11.mystock.framework.environment.EnvironmentManager.Environment.Test
import com.section11.mystock.framework.resource.ResourceProviderImpl
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.model.WatchlistScreenUiModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.model.mapper.StockSearchResultUiMapper
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import com.section11.mystock.ui.singlestock.SingleStockViewModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel.SingleStockUiState.SingleStockFetched
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

private const val DEFAULT_STOCKS_LIST_SIZE = 5
private const val DEFAULT_GRAPH_NODES_SIZE = 200
private const val DEFAULT_SEARCH_BAR_RESULTS = 7

@Suppress("MagicNumber") // Suppress warning for mock information on the preview
class FakeRepositoryForPreviews(context: Context) {

    private val resourceProvider = ResourceProviderImpl(context)
    private val stockWatchlistUiModelMapper = StockWatchlistUiModelMapper(resourceProvider, Test)
    private val singleStockUiMapper = StockInformationUiModelMapper(resourceProvider)
    private val stockSearchUiModelMapper = StockSearchResultUiMapper(resourceProvider)

    fun getStockWatchlist(size: Int = DEFAULT_STOCKS_LIST_SIZE): WatchlistScreenUiModel {
        val stocks = List(size) { index ->
            Stock("Microsoft $index", "MSFT", "NASDAQ")
        }
        return stockWatchlistUiModelMapper.mapToUiModel(stocks)
    }

    fun getSingleStockInfoStateSuccess(): StateFlow<SingleStockViewModel.SingleStockUiState> {
        return MutableStateFlow<SingleStockViewModel.SingleStockUiState>(
            SingleStockFetched(getSingleStockInformationUiModel())
        )
    }

    fun getSingleStockInformationUiModel(): StockInformationUiModel {
        val stockInfo = StockInformation(
            summary = getSummary(),
            graph = GraphInformation(
                graphNodes = List(DEFAULT_GRAPH_NODES_SIZE) { index ->
                    GraphNode(
                        price = Random.nextDouble(100.0, 200.0),
                        dateLabel = "Date $index:$index"
                    )
                },
                edgeLabels = Pair("08:26", "08:50"),
                horizontalAxisLabels = listOf("08:31", "08:36", "08:40", "08:45")
            )
        )

        return singleStockUiMapper.mapToUiModel(stockInfo)
    }

    private fun getSummary(): Summary {
        return Summary(
            title = "Apple Inc.",
            stock = "AAPL",
            exchange = "NASDAQ",
            price = "426.32",
            currency = "$",
            priceMovement = PriceMovement(
                percentage = 2.561525,
                value = 10.647491,
                movement = "Up"
            )
        )
    }

    fun getSearchResultsWithExactMatch(): List<StockSearchResultUiModel> {
        return stockSearchUiModelMapper.mapToUiModel(StockSearchResults(
            exactMatch = getSummary(),
            closeMatchStocks = null
        )
        )
    }

    fun getSearchResultWithNoExactMatch(): List<StockSearchResultUiModel> {
        return stockSearchUiModelMapper.mapToUiModel(StockSearchResults(
            exactMatch = null,
            closeMatchStocks = List(DEFAULT_SEARCH_BAR_RESULTS) { index ->
                CloseMatchStock(
                    title = "Apple Inc.",
                    stock = "AAPL$index",
                    extractedPrice = "426.32",
                    currency = "$",
                    priceMovement = CloseMatchStockPriceMovement(
                        percentage = 2.561525,
                        movement = "Up"
                    )
                )
            }
        )
        )
    }
}
