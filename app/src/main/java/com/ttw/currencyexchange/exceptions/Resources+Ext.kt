package com.ttw.currencyexchange.exceptions

import android.content.res.Resources
import com.ttw.currencyexchange.R
import java.util.*

fun Resources.getFlagResource(flagName: String): Int {
    val id = getIdentifier(
        "_${flagName.toLowerCase(Locale.ROOT)}",
        "drawable",
        "com.ttw.currencyexchange"
    )
    if (id == 0) {
        return R.drawable._no_flag
    }
    return id
}