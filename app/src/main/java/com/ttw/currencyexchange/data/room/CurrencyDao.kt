package com.ttw.currencyexchange.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ttw.currencyexchange.model.Currency

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(data: List<Currency>)

    @Query("select * from currencyTable")
    suspend fun getCurrency(): List<Currency>
}