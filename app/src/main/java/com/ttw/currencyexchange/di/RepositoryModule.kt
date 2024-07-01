package com.ttw.currencyexchange.di
import com.ttw.currencyexchange.repository.CurrencyRepository
import com.ttw.currencyexchange.repository.CurrencyRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repo: CurrencyRepositoryImp): CurrencyRepository

}
