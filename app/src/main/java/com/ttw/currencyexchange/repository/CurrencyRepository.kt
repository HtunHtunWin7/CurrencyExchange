package com.ttw.currencyexchange.repository

import com.ttw.currencyexchange.model.Currency
import com.ttw.currencyexchange.model.CurrencyTypes
import com.ttw.currencyexchange.model.CurrentExchangeRates
import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    // Remote-> Retrofit
    suspend fun getCurrentExchangeRates(): Flow<CurrentExchangeRates>
    suspend fun getCurrencyTypes(): Flow<CurrencyTypes>

    // Local-> Room
    suspend fun getSavedExchangeRates(): List<Currency>
    suspend fun updateAllExchangeRates(saveList: List<Currency>)
}