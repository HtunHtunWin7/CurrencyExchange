package com.ttw.currencyexchange.repository

import com.ttw.currencyexchange.repository.CurrencyRepository
import com.ttw.currencyexchange.data.api.CurrencyService
import com.ttw.currencyexchange.data.room.CurrencyDb
import com.ttw.currencyexchange.model.CurrentExchangeRates
import com.ttw.exchangecurrency.exceptions.AppException
import com.ttw.currencyexchange.exceptions.NetworkException
import com.ttw.currencyexchange.model.ApiError
import com.ttw.currencyexchange.model.Currency
import com.ttw.currencyexchange.model.CurrencyTypes
import com.ttw.currencyexchange.utils.parseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CurrencyRepositoryImp @Inject constructor(private val service: CurrencyService, private val db: CurrencyDb):
    CurrencyRepository {
    override suspend fun getCurrentExchangeRates(): Flow<CurrentExchangeRates> {
        var response = CurrentExchangeRates()
        return flow {
            withContext(Dispatchers.IO) {
                try {
                    response = service.getCurrentExchangeRates()
                }  catch (e: Throwable) {
                    when (e) {
                        is IOException -> throw NetworkException("Connection Issue")
                        is HttpException -> throw when {
                            e.response()
                                ?.code() == 500 -> AppException("Server Error")

                            e.response()
                                ?.code() == 403 -> AppException("DENIED")

                            e.response()?.code() == 406 -> AppException("Another Login")
                            else ->{
                                val errorBody = (e as? HttpException)?.response()?.errorBody()
                                val apiError: ApiError? = errorBody?.parseError()
                                AppException(apiError?.message ?: e.localizedMessage)
                            }
                        }

                        else ->{
                            val errorBody = (e as? HttpException)?.response()?.errorBody()
                            val apiError: ApiError? = errorBody?.parseError()
                            AppException(apiError?.message ?: e.localizedMessage)
                        }
                    }
                }
            }
            emit(response)
        }
    }

    override suspend fun getCurrencyTypes(): Flow<CurrencyTypes> {
        var response = CurrencyTypes()
        return flow {
            withContext(Dispatchers.IO) {
                try {
                    response = service.getCurrencyTypes()
                }  catch (e: Throwable) {
                    when (e) {
                        is IOException -> throw NetworkException("Connection Issue")
                        is HttpException -> throw when {
                            e.response()
                                ?.code() == 500 -> AppException("Server Error")

                            e.response()
                                ?.code() == 403 -> AppException("DENIED")

                            e.response()?.code() == 406 -> AppException("Another Login")
                            else ->{
                                val errorBody = (e as? HttpException)?.response()?.errorBody()
                                val apiError: ApiError? = errorBody?.parseError()
                                AppException(apiError?.message ?: e.localizedMessage)
                            }
                        }

                        else ->{
                            val errorBody = (e as? HttpException)?.response()?.errorBody()
                            val apiError: ApiError? = errorBody?.parseError()
                            AppException(apiError?.message ?: e.localizedMessage)
                        }
                    }
                }
            }
            emit(response)
        }
    }

    override suspend fun getSavedExchangeRates(): List<Currency> = db.dao().getCurrency()
    override suspend fun updateAllExchangeRates(saveList: List<Currency>) = db.dao().insertData(saveList)
}