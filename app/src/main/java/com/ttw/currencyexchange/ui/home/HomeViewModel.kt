package com.ttw.currencyexchange.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ttw.currencyexchange.model.Currency
import com.ttw.currencyexchange.model.CurrencyTypes
import com.ttw.currencyexchange.model.CurrentExchangeRates
import com.ttw.currencyexchange.repository.CurrencyRepository
import com.ttw.currencyexchange.vo.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: CurrencyRepository) : ViewModel() {

    val amount = MutableLiveData<String>()

    private val _currency by lazy {
        MutableLiveData<Resource<List<Currency>>>()
    }
    val currency: LiveData<Resource<List<Currency>>>
        get() = _currency

    private val _exchangeRates by lazy {
        MutableLiveData<Resource<CurrentExchangeRates>>()
    }
    val exchangeRates: LiveData<Resource<CurrentExchangeRates>>
        get() = _exchangeRates

    private val _currencyTypes by lazy {
        MutableLiveData<Resource<CurrencyTypes>>()
    }
    val currencyTypes : LiveData<Resource<CurrencyTypes>>
        get() = _currencyTypes


    fun fetchCurrencyExchangeRate() = viewModelScope.launch() {
        repository.getCurrentExchangeRates()
            .onStart { _currency.postValue(Resource.loading(data = null))}
            .catch {  _currency.postValue(Resource.error(
                data = null,
                message = it.message.toString()
            ) )}
            .collect {
                delay(2000)
                _exchangeRates.postValue(Resource.success(data = it))
            }
    }

    fun fetchCurrencyTypes() = viewModelScope.launch() {
        repository.getCurrencyTypes()
            .onStart { _currency.postValue(Resource.loading(data = null)) }
            .catch {  _currency.postValue(Resource.error(
                data = null,
                message = it.message.toString()
            ) ) }
            .collect{
                _currencyTypes.postValue(Resource.success(data = it))
            }
    }

    fun saveInLocal(data: List<Currency>) = viewModelScope.launch {
        repository.updateAllExchangeRates(data)
    }

    fun fetchFromLocal() = viewModelScope.launch {
        repository.getSavedExchangeRates().let {
          _currency.postValue(Resource.success(data = it))
        }
    }

    fun assembleCurrencyData(
        exchangeRates: CurrentExchangeRates,
        currencyTypes: CurrencyTypes
    ){
        val list = ArrayList<Currency>()
        for (entry in currencyTypes.currencies) {
            val currency = Currency()
            currency.code = entry.key
            currency.name = entry.value
            if (exchangeRates.quotes[exchangeRates.source+entry.key] == null ){
                currency.rate = 1.0
            }else{
                currency.rate = exchangeRates.quotes[exchangeRates.source+entry.key]!!
            }
            list.add(currency)
        }
        _currency.postValue(Resource.success(data = list))
    }

}