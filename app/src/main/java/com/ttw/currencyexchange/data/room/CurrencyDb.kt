package com.ttw.currencyexchange.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ttw.currencyexchange.model.Currency

@Database(
    entities = [Currency::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDb : RoomDatabase() {
    abstract fun dao(): CurrencyDao
}
