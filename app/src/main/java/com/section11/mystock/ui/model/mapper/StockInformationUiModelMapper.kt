package com.section11.mystock.ui.model.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.ui.graphics.Color
import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockInformation.GraphInformation
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph
import com.section11.mystock.domain.models.StockInformation.KnowledgeGraph.KeyStats.ClimateChange
import com.section11.mystock.domain.models.StockInformation.Summary
import com.section11.mystock.ui.common.extensions.toPercentageFormat
import com.section11.mystock.ui.common.model.SnackBarModel
import com.section11.mystock.ui.model.ActionableIconUiModel
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.GraphUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.AboutUiModel.DescriptionUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.AboutUiModel.InfoUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel.ClimateChangeScoreUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel.StatUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.KnowledgeGraphUiModel.KeyStatsUiModel.TagUiModel
import com.section11.mystock.ui.model.StockInformationUiModel.SummaryUiModel
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AddToWatchlist
import com.section11.mystock.ui.singlestock.SingleStockViewModel.ActionableIconState.AlreadyAddedToWatchlist
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import com.section11.mystock.ui.theme.gray50
import javax.inject.Inject

private const val MOVEMENT_UP = "Up"
private const val DEFAULT_HORIZONTAL_LINES = 3

class StockInformationUiModelMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun mapToUiModel(stockInformation: StockInformation): StockInformationUiModel {
        return StockInformationUiModel(
            summaryUiModel = getSummaryUiModel(stockInformation.summary),
            graphModel = getGraphModel(stockInformation.graph),
            knowledgeGraph = getKnowledgeGraph(stockInformation.knowledgeGraph)
        )
    }

    private fun getSummaryUiModel(summary: Summary): SummaryUiModel {
        return with(summary) {
            SummaryUiModel(
                title = title,
                stockSymbolLabel = getStockSymbolLabel(symbol),
                symbol = symbol,
                exchangeLabel = getExchangeLabel(exchange),
                exchange = exchange,
                priceLabel = getPriceLabel(price, currency),
                priceMovementTitle = getPriceMovementTitle(),
                priceMovementValueLabel = getPriceMovementLabel(
                    priceMovement.movement,
                    priceMovement.value
                ),
                priceMovementPercentage = getPriceMovementPercentage(
                    priceMovement.movement,
                    priceMovement.percentage
                ),
                priceMovementColor = getPriceMovementColor(priceMovement.movement),
            )
        }
    }

    private fun getStockSymbolLabel(symbol: String): String {
        return resourceProvider.getString(R.string.single_stock_screen_symbol_text, symbol)
    }

    private fun getExchangeLabel(exchange: String): String {
        return resourceProvider.getString(R.string.single_stock_screen_exchange_text, exchange)
    }

    private fun getPriceLabel(price: String, currency: String): String {
        return resourceProvider.getString(R.string.single_stock_screen_price_text, currency, price)
    }

    private fun getPriceMovementTitle(): String {
        return resourceProvider.getString(R.string.single_stock_screen_price_movement_title)
    }

    private fun getPriceMovementLabel(
        movement: String,
        priceMovementValue: Double
    ): String {
        val arrow = if (isPriceUp(movement)) {
            resourceProvider.getString(R.string.single_stock_screen_arrow_up)
        } else {
            resourceProvider.getString(R.string.single_stock_screen_arrow_down)
        }

        return resourceProvider.getString(
            R.string.single_stock_screen_price_movement_label,
            arrow,
            priceMovementValue.toPercentageFormat()
        )
    }

    private fun getPriceMovementPercentage(
        priceMovement: String,
        priceMovementPercentage: Double
    ): String {
        val sign = if (isPriceUp(priceMovement)){
            resourceProvider.getString(R.string.single_stock_screen_plus)
        } else {
            resourceProvider.getString(R.string.single_stock_screen_minus)
        }
        return resourceProvider.getString(
            R.string.single_stock_screen_price_movement_percentage,
            sign,
            priceMovementPercentage.toPercentageFormat()
        )
    }



    private fun isPriceUp(priceMovement: String) = priceMovement == MOVEMENT_UP

    private fun getPriceMovementColor(movement: String): Color {
        return if (isPriceUp(movement)) {
            Green
        } else {
            Red
        }
    }

    private fun getGraphModel(graphInformation: GraphInformation): GraphUiModel {
        return with(graphInformation) {
            GraphUiModel(
                graphPoints = graphNodes.map { it.price },
                graphEdgeLabels = edgeLabels,
                graphHorizontalLabels = horizontalAxisLabels,
                graphGridVerticalLinesAmount = horizontalAxisLabels.size,
                graphGridHorizontalLinesAmount = DEFAULT_HORIZONTAL_LINES
            )
        }
    }

    private fun getKnowledgeGraph(knowledgeGraph: KnowledgeGraph): KnowledgeGraphUiModel {
        return KnowledgeGraphUiModel(
            keyStats = KeyStatsUiModel(
                stats = knowledgeGraph.keyStats.stats.map {
                    StatUiModel(
                        label = it.label,
                        description = it.description,
                        value = it.value
                    )
                },
                tags = knowledgeGraph.keyStats.tags.map {
                    TagUiModel(
                        text = it.text,
                        description = it.description,
                        link = it.link,
                        tagColor = gray50,
                        onTapSnackBarModel = it.link?.let { link ->
                            SnackBarModel(
                                message = resourceProvider.getString(
                                    R.string.single_stock_screen_snackbar_with_link_title,
                                    link
                                ),
                                actionLabel = resourceProvider.getString(
                                    R.string.single_stock_screen_snackbar_with_link_action_label
                                ),
                                link = link
                            )
                        }
                    )
                },
                climateChange = getClimateChangeSection(knowledgeGraph.keyStats.climateChange)
            ),
            about = knowledgeGraph.about.map {
                KnowledgeGraphUiModel.AboutUiModel(
                    title = it.title,
                    description = DescriptionUiModel(
                        snippet = it.description.snippet,
                        link = it.description.link,
                        linkText = it.description.linkText
                    ),
                    info = it.info.map { info ->
                        InfoUiModel(
                            label = info.label,
                            value = info.value,
                            link = info.link
                        )
                    }
                )
            }
        )
    }

    private fun getClimateChangeSection(climateChange: ClimateChange?): ClimateChangeScoreUiModel? {
        return climateChange?.let { climateChangeInfo ->
            ClimateChangeScoreUiModel(
                title = resourceProvider.getString(
                    R.string.single_stock_screen_climate_score_title
                ),
                score = climateChangeInfo.score,
                link = climateChangeInfo.link,
                iconId = R.drawable.ic_launcher_foreground
            )
        }
    }

    fun getActionableIconUiModel(
        stockSummary: Summary,
        isStockInWatchlist: Boolean
    ): ActionableIconState {

        val (icon, contentDesc) = if (isStockInWatchlist) {
            Icons.Default.CheckCircle to resourceProvider.getString(
                R.string.single_stock_screen_actionable_icon_remove_content_description
            )
        } else {
            Icons.Default.AddCircle to resourceProvider.getString(
                R.string.single_stock_screen_actionable_icon_add_content_description
            )
        }


        return with(stockSummary) {
            val iconUiModel = ActionableIconUiModel(title, symbol, exchange, icon, contentDesc)
            if (isStockInWatchlist) {
                AlreadyAddedToWatchlist(iconUiModel)
            } else {
                AddToWatchlist(iconUiModel)
            }
        }
    }

    fun updateIconOnUiModel(conUiModel: ActionableIconUiModel): ActionableIconUiModel {
        with(conUiModel) {
            val newIconVector = if (iconVector == Icons.Default.CheckCircle) {
                Icons.Default.AddCircle
            } else {
                Icons.Default.CheckCircle
            }
            return ActionableIconUiModel(title, symbol, exchange, newIconVector, contentDescription)
        }
    }
}
