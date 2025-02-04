package com.section11.mystock.data.mapper

import com.section11.mystock.data.dto.CloseMatchPriceMovementResponse
import com.section11.mystock.data.dto.CloseMatchStockResponse
import com.section11.mystock.data.dto.StockSearchResponse
import com.section11.mystock.domain.common.Const.COLON
import com.section11.mystock.domain.models.CloseMatchStock
import com.section11.mystock.domain.models.CloseMatchStockPriceMovement
import com.section11.mystock.domain.models.StockSearchResults

fun StockSearchResponse.toStockSearchResults(): StockSearchResults {
    return StockSearchResults(
        exactMatch = summary?.toSummary(),
        closeMatchStocks = closeMatchStock?.map { it.toCloseMatchStock() }
    )
}

fun CloseMatchStockResponse.toCloseMatchStock(): CloseMatchStock {
    val symbol = stock.substringBefore(COLON)
    val exchange = stock.substringAfter(COLON)
    return CloseMatchStock(
        title = title,
        symbol = symbol,
        exchange = exchange,
        extractedPrice = extractedPrice,
        currency = currency,
        priceMovement = priceMovement.toCloseMatchStockPriceMovement()
    )
}

fun CloseMatchPriceMovementResponse.toCloseMatchStockPriceMovement(): CloseMatchStockPriceMovement {
    return CloseMatchStockPriceMovement(
        percentage = percentage,
        movement = movement
    )
}
