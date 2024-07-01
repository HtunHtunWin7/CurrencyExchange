package com.ttw.currencyexchange.data.api

import com.ttw.currencyexchange.model.CurrencyTypes
import com.ttw.currencyexchange.model.CurrentExchangeRates
import retrofit2.http.GET

interface CurrencyService {

    @GET("live")
    suspend fun getCurrentExchangeRates(): CurrentExchangeRates

    @GET("list")
    suspend fun getCurrencyTypes(): CurrencyTypes
}