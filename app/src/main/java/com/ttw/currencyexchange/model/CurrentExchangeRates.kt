package com.ttw.currencyexchange.model

import com.google.gson.annotations.SerializedName

data class CurrentExchangeRates(

    @SerializedName("timestamp")
    var timestamp: Long = 0,

    @SerializedName("quotes")
    var quotes: HashMap<String, Double> = HashMap(),

    @SerializedName("source")
    var source: String? = ""
)