package com.ttw.currencyexchange

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrencyApp : Application(){
    override fun onCreate() {
        super.onCreate()
    }
}