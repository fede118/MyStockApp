package com.section11.mystock.ui.model.mapper

import androidx.compose.ui.graphics.Color
import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.models.GraphInformation
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.ui.common.extentions.toPercentageFormat
import com.section11.mystock.ui.model.GraphUiModel
import com.section11.mystock.ui.model.StockInformationUiModel
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import javax.inject.Inject

private const val MOVEMENT_UP = "Up"
private const val DEFAULT_HORIZONTAL_LINES = 3

class StockInformationUiModelMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun mapToUiModel(stockInformation: StockInformation): StockInformationUiModel {
        return with(stockInformation.summary) {
            StockInformationUiModel(
                title = title,
                stockSymbolLabel = getStockSymbolLabel(stock),
                exchangeLabel = getExchangeLabel(exchange),
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
                graphModel = getGraphModel(stockInformation.graph)
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
        return resourceProvider.getString(R.string.single_stock_screen_price_text, price, currency)
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
        return GraphUiModel(
            graphPoints = graphInformation.graphNodes.map { it.price },
            graphHorizontalLabels = graphInformation.horizontalAxisLabels,
            graphBackgroundVerticalLinesAmount = graphInformation.horizontalAxisLabels.size,
            graphBackgroundHorizontalLinesAmount = DEFAULT_HORIZONTAL_LINES
        )
    }
}
