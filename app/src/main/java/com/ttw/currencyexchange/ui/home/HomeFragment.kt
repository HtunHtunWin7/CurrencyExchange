package com.ttw.currencyexchange.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.ttw.currencyexchange.R
import com.ttw.currencyexchange.databinding.FragmentHomeBinding
import com.ttw.currencyexchange.exceptions.errorAlert
import com.ttw.currencyexchange.model.CurrentExchangeRates
import com.ttw.currencyexchange.ui.adapters.CurrencyListAdapter
import com.ttw.currencyexchange.ui.adapters.SpinnerAdapter
import com.ttw.currencyexchange.utils.LAST_FETCH_TIMESTAMP
import com.ttw.currencyexchange.utils.getCurrentTimeStampInMillis
import com.ttw.currencyexchange.utils.getPreferenceLong
import com.ttw.currencyexchange.utils.hideSoftKeyboard
import com.ttw.currencyexchange.utils.setPreferenceLong
import com.ttw.currencyexchange.utils.textInputAsFlow
import com.ttw.currencyexchange.vo.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private var exchangeRate: CurrentExchangeRates ?= null
    private lateinit var spinnerAdapter: SpinnerAdapter
    private lateinit var currencyListAdapter: CurrencyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        spinnerAdapter = SpinnerAdapter(requireContext())
        currencyListAdapter = CurrencyListAdapter()
        bindRecycler()
        check_30_min_DataLoad()
        observeExchangeRates()
        observeCurrencyTypes()
        observeCurrency()
        viewModel.amount.observe(viewLifecycleOwner, amountObserver)
        amountInputObserver()
        return binding.root
    }

    private fun bindRecycler(){
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = currencyListAdapter
        }
        binding.currencyListSpinner.adapter = spinnerAdapter

    }

    private fun amountInputObserver(){
        binding.currencyTo
            .textInputAsFlow()
            .map { it }
            .debounce(500)
            .onEach {
                viewModel.amount.postValue(it.toString())
            }
            .launchIn(lifecycleScope)
    }

    private val amountObserver = Observer<String> { amount ->
        var parsed = 1.0
        if (amount.isNotEmpty()) {
            parsed = amount.toDouble()
        }
        currencyListAdapter.convert(parsed)
    }


    private fun check_30_min_DataLoad(){
        val period = 30 * 60
       val last_fetch_time = getPreferenceLong(requireContext(), LAST_FETCH_TIMESTAMP)
        Log.d("last fetch time #####", (last_fetch_time+period).toString())
        Log.d("current time #####", getCurrentTimeStampInMillis().toString())
        if ((last_fetch_time + period) > getCurrentTimeStampInMillis()){
            viewModel.fetchFromLocal()
        }else{
            viewModel.fetchCurrencyExchangeRate()
        }

    }

    private fun observeExchangeRates(){
        viewModel.exchangeRates.observe(viewLifecycleOwner){
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        exchangeRate = resource.data
                        setPreferenceLong(requireContext(), LAST_FETCH_TIMESTAMP, getCurrentTimeStampInMillis())
                        //limitation rate reached if fetch without delay
                        viewModel.fetchCurrencyTypes()

                    }
                    Status.ERROR -> {}
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun observeCurrencyTypes(){
        viewModel.currencyTypes.observe(viewLifecycleOwner){
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        if (exchangeRate != null){
                            viewModel.assembleCurrencyData(exchangeRate!!, resource.data!!)
                        }
                    }
                    Status.ERROR -> {}
                    Status.LOADING -> {}
                }
            }
        }
    }

    private fun observeCurrency(){
        viewModel.currency.observe(viewLifecycleOwner){
            it.let { resource ->
                when(resource.status){
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        resource.data?.let { it1 -> viewModel.saveInLocal(it1) }
                        currencyListAdapter.setDataList(resource.data!!)
                        Log.d("Currency List @@@", resource.data?.size.toString())
                        spinnerAdapter.setDataList(resource.data!!)
                        binding.currencyListSpinner.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View,
                                    pos: Int,
                                    id: Long
                                ) {
                                    currencyListAdapter?.updateSelectedCurrencyRate(resource.data[pos].rate)
                                }
                                override fun onNothingSelected(parent: AdapterView<*>?) {

                                }
                            }
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        errorAlert(requireContext(), it.message.toString())
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}