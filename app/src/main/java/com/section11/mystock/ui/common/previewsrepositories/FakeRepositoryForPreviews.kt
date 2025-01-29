package com.section11.mystock.ui.common.previewsrepositories

import android.content.Context
import com.section11.mystock.domain.models.CloseMatchStock
import com.section11.mystock.domain.models.CloseMatchStockPriceMovement
import com.section11.mystock.domain.models.Stock
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation.GraphNode
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.About
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.ClimateChange
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.Stat
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.Tag
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.domain.models.StockInformation.Summary.PriceMovement
import com.section11.mystock.domain.models.StockSearchResults
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
            ),
            knowledgeGraph = KnowledgeGraph(
                keyStats = KnowledgeGraph.KeyStats(
                    tags = listOf(
                        Tag("Climate leader","description"),
                        Tag("Stock","description"),
                        Tag("US listed security","description"),
                        Tag("US headquartered","description")
                    ),
                    stats = listOf(
                        Stat("Day Range","description1","$190.68 - $195.40"),
                        Stat("Year Range","description1","$130.67 - $202.29"),
                        Stat("Market Cap","description2","2.40T USD"),
                        Stat("Avg Volume","description2","28.65M"),
                        Stat("P/E Ratio","description2","25.84"),
                        Stat("Dividend Yield","description2","0.41%"),
                        Stat("Primary Exchange","description2","NASDAQ")
                    ),
                    climateChange = ClimateChange(
                        score = "A",
                        link = "link"
                    )
                ),
                about = listOf(
                    About(
                        title = "title",
                        description = About.Description(
                            snippet = "snippet",
                            link = "link",
                            linkText = "linkText"
                        ),
                        info = listOf(
                            About.Info(
                                label = "label",
                                value = "value",
                                link = "link"
                            )
                        )
                    )
                )
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
