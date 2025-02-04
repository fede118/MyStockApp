package com.section11.mystock.domain.repositories

import com.section11.mystock.domain.models.StockInformation
import com.section11.mystock.domain.models.StockSearchResults

/**
 * Repository for retrieving stocks information.
 *
 * When the stock is already known, the symbol is precise use [getStockInformation] since the response
 * of the service when the query is exact is different than when you search with less precision.
 * For ex. you search AAPL or TSLA, etc. Which are correct, exact symbols for a stock
 * If the query is not exact, use [searchStock]. For ex. if you search "Google" or "microsoft".
 */
interface StocksInformationRepository {

    /**
     * Gets the stock information for the given [symbolColonExchange]. The symbol needs to be exact and correct.
     * with this format symbol:exchange Ex: AAPL:NASDAQ
     */
    suspend fun getStockInformation(symbolColonExchange: String): StockInformation

    /**
     * Searches for the stock with the given [query]. In this one the query can be less precise.
     * Since the query can be anything, expect StockSearchResults to have null information in case
     * nothing is found
     */
    suspend fun searchStock(query: String): StockSearchResults
}
