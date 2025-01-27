package com.section11.mystock.ui.model.mapper

import androidx.compose.ui.graphics.Color
import com.section11.mystock.R
import com.section11.mystock.common.resources.ResourceProvider
import com.section11.mystock.domain.common.Const.COLON
import com.section11.mystock.domain.models.StockSearchResults
import com.section11.mystock.ui.common.extentions.toPercentageFormat
import com.section11.mystock.ui.model.StockSearchResultUiModel
import com.section11.mystock.ui.theme.Green
import com.section11.mystock.ui.theme.Red
import com.section11.mystock.ui.theme.RedIntense
import javax.inject.Inject

private const val UP = "up"
private const val DOWN = "down"

class StockSearchResultUiMapper @Inject constructor(
    private val resourceProvider: ResourceProvider
) {

    fun mapToUiModel(stockSearchResult: StockSearchResults?): List<StockSearchResultUiModel> {
        val resultsList = mutableListOf<StockSearchResultUiModel>()
        stockSearchResult?.run {
            exactMatch?.let {
                resultsList.add(
                    StockSearchResultUiModel(
                        title = it.title,
                        symbolColonExchange = it.stock + COLON + it.exchange,
                        symbolBoxColor = RedIntense,
                        priceLabel = "${it.currency}${it.price}",
                        priceMovementSymbol = mapPriceMovementSymbol(it.priceMovement.movement),
                        priceMovementColor = mapPriceMovementColor(it.priceMovement.movement),
                        percentage = "${it.priceMovement.percentage.toPercentageFormat()}%"
                    )
                )
            }

            closeMatchStocks?.forEach {
                resultsList.add(
                    StockSearchResultUiModel(
                        title = it.title,
                        symbolColonExchange = it.stock,
                        symbolBoxColor = RedIntense,
                        priceLabel = "${it.currency}${it.extractedPrice}",
                        priceMovementSymbol = mapPriceMovementSymbol(it.priceMovement.movement),
                        priceMovementColor = mapPriceMovementColor(it.priceMovement.movement),
                        percentage = resourceProvider.getString(
                            R.string.with_percentage,
                            it.priceMovement.percentage.toPercentageFormat()
                        )
                    )
                )
            }
        }

        return resultsList
    }

    private fun mapPriceMovementSymbol(movement: String): String {
        return when (movement.lowercase()) {
            UP -> resourceProvider.getString(R.string.search_bar_results_up_arrow)
            DOWN -> resourceProvider.getString(R.string.search_bar_results_down_arrow)
            else -> String()
        }
    }

    private fun mapPriceMovementColor(movement: String): Color {
        return when (movement.lowercase()) {
            UP -> Green
            DOWN -> Red
            else -> Color.Gray
        }
    }
}
