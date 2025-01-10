package com.section11.mystock.data.service

import com.section11.mystock.data.dto.StockInformationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val SEARCH_PATH = "search"

interface StocksInformationService {

    @GET(SEARCH_PATH)
    suspend fun getStockInformation(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("engine") engine: String = "google_finance"
    ): Response<StockInformationResponse>
}
