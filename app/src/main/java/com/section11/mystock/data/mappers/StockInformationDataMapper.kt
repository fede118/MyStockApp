package com.section11.mystock.data.mappers

import com.section11.mystock.data.dto.PriceMovementResponse
import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.SummaryResponse
import com.section11.mystock.domain.models.PriceMovement
import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.Summary

fun StockInformationResponse.toStockInformation(): StockInformation {
    return StockInformation(
        summary = summary.toSummary()
    )
}

private fun SummaryResponse.toSummary(): Summary {
    return Summary(
        title = title,
        stock = stock,
        exchange = exchange,
        price = price,
        currency = currency,
        priceMovement = priceMovement.toPriceMovement()
    )
}

private fun PriceMovementResponse.toPriceMovement(): PriceMovement {
    return PriceMovement(
        percentage = percentage,
        value = value,
        movement = movement
    )
}
