package com.ttw.currencyexchange.di

import android.content.Context
import androidx.room.Room
import com.ttw.currencyexchange.data.room.CurrencyDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, CurrencyDb::class.java, "currency_db").fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideDao(db: CurrencyDb) = db.dao()


}