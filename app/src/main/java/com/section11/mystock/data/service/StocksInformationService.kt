package com.section11.mystock.data.service

import com.section11.mystock.data.dto.StockInformationResponse
import com.section11.mystock.data.dto.StockSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val SEARCH_PATH = "search"
private const val API_KEY = "api_key"
private const val QUERY = "q"
private const val ENGINE = "engine"
private const val DEFAULT_ENGINE = "google_finance"

interface StocksInformationService {

    @GET(SEARCH_PATH)
    suspend fun getStockInformation(
        @Query(API_KEY) apiKey: String,
        @Query(QUERY) query: String,
        @Query(ENGINE) engine: String = DEFAULT_ENGINE
    ): Response<StockInformationResponse>

    @GET(SEARCH_PATH)
    suspend fun searchStock(
        @Query(API_KEY) apiKey: String,
        @Query(QUERY) query: String,
        @Query(ENGINE) engine: String = DEFAULT_ENGINE
    ): Response<StockSearchResponse>
}
