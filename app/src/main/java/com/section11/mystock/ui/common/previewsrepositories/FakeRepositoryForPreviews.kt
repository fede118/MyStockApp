package com.section11.mystock.ui.common.previewsrepositories

import android.content.Context
import com.section11.mystock.domain.models.GraphInformation
import com.section11.mystock.domain.models.GraphNode
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.Summary
import com.section11.mystock.framework.resource.ResourceProviderImpl
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState
import com.section11.mystock.ui.home.HomeViewModel.SingleStockInformationState.FetchedSingleStockInfo
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.WatchlistStockModel
import com.section11.mystock.ui.model.mapper.StockInformationUiModelMapper
import com.section11.mystock.ui.model.mapper.StockWatchlistUiModelMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

private const val DEFAULT_STOCKS_LIST_SIZE = 5
private const val DEFAULT_GRAPH_NODES_SIZE = 200

@Suppress("MagicNumber") // Suppress warning for mock information on the preview
class FakeRepositoryForPreviews(context: Context) {

    private val stockWatchlistUiModelMapper = StockWatchlistUiModelMapper(ResourceProviderImpl(context))
    private val singleStockUiMapper = StockInformationUiModelMapper(ResourceProviderImpl(context))

    fun getStockWatchlist(size: Int = DEFAULT_STOCKS_LIST_SIZE): List<WatchlistStockModel> {
        val stocks = List(size) { index ->
            Stock("Microsoft $index", "MSFT")
        }
        return stockWatchlistUiModelMapper.mapToUiModel(stocks)


    }

    fun getSingleStockInfoStateSuccess(): StateFlow<SingleStockInformationState> {
        return MutableStateFlow<SingleStockInformationState>(
            FetchedSingleStockInfo(getSingleStockInformationUiModel())
        )
    }

    fun getSingleStockInformationUiModel(): StockInformationUiModel {
        val stockInfo = StockInformation(
            Summary(
                title = "Apple Inc.",
                stock = "AAPL",
                exchange = "NASDAQ",
                price = "$426.32",
                currency = "$",
                priceMovement = PriceMovement(
                    percentage = 2.561525,
                    value = 10.647491,
                    movement = "Up"
                )
            ),
            graph = GraphInformation(
                graphNodes = List(DEFAULT_GRAPH_NODES_SIZE) { index ->
                    GraphNode(
                        price = Random.nextDouble(100.0, 200.0),
                        dateLabel = "Date $index:$index"
                    )
                },
                horizontalAxisLabels = listOf("08:31", "08:36", "08:40", "08:45")
            )
        )

        return singleStockUiMapper.mapToUiModel(stockInfo)
    }
}
