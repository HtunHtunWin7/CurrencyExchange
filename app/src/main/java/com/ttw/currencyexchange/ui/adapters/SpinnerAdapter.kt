package com.ttw.currencyexchange.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.ttw.currencyexchange.R
import com.ttw.currencyexchange.databinding.ItemSpinnerCurrencyBinding
import com.ttw.currencyexchange.exceptions.getFlagResource
import com.ttw.currencyexchange.model.Currency

class SpinnerAdapter(context: Context) :
    ArrayAdapter<String>(context, R.layout.item_spinner_currency, arrayListOf()) {

    private var currencies: List<Currency> = ArrayList()

    fun setDataList(currencies: List<Currency>) {
        this.currencies = currencies
        notifyDataSetChanged()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    override fun getCount(): Int {
        return currencies.size
    }

    private fun getCustomView(position: Int, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val binding = ItemSpinnerCurrencyBinding.inflate(inflater, parent, false)
        val resource = context.resources.getFlagResource(currencies[position].code)
        binding.flag.setImageDrawable(ContextCompat.getDrawable(binding.flag.context, resource))
        binding.currencyCodeTextView.text = currencies[position].name

        binding.executePendingBindings()
        return binding.root
    }
}