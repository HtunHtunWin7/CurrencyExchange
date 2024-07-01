package com.ttw.currencyexchange.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ttw.currencyexchange.databinding.ItemCurrencyCardBinding
import com.ttw.currencyexchange.exceptions.getFlagResource
import com.ttw.currencyexchange.model.Currency
import java.text.DecimalFormat

class CurrencyListAdapter : RecyclerView.Adapter<CurrencyListAdapter.AdapterViewHolder>() {

    private var dataSet: List<Currency> = ArrayList()
    private var amountToConvert = 1.0
    private var selectedCurrencyExchangeRate = 1.0

    fun setDataList(dataSet: List<Currency>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    fun updateSelectedCurrencyRate(rate: Double) {
        this.selectedCurrencyExchangeRate = rate
        notifyDataSetChanged()
    }

    fun convert(amountToConvert: Double) {
        this.amountToConvert = amountToConvert
        notifyDataSetChanged()
    }

    private fun calculateExchangeAmount(position: Int): Double {
        return dataSet[position].rate.times(amountToConvert).div(selectedCurrencyExchangeRate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyCardBinding.inflate(inflater, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val currency = dataSet[position]
        val dec = DecimalFormat("#,###.##")
        currency.amount = dec.format(calculateExchangeAmount(position))
        holder.bind(currency)
    }

    class AdapterViewHolder(private val binding: ItemCurrencyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            val resource = binding.root.context.resources.getFlagResource(currency.code)
            binding.flag.setImageDrawable(ContextCompat.getDrawable(binding.flag.context, resource))
            binding.currencyCodeTextView.text = currency.code
            binding.tvAmount.text = currency.amount
            binding.executePendingBindings()
        }
    }
}