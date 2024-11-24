package com.abadzheva.cryptoapp.di

import android.app.Application
import com.abadzheva.cryptoapp.data.database.AppDatabase
import com.abadzheva.cryptoapp.data.database.CoinInfoDao
import com.abadzheva.cryptoapp.data.network.ApiFactory
import com.abadzheva.cryptoapp.data.network.ApiService
import com.abadzheva.cryptoapp.data.repository.CoinRepositoryImpl
import com.abadzheva.cryptoapp.domain.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    @ApplicationScope
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    companion object {
        @Provides
        @ApplicationScope
        fun provideCoinInfoDao(application: Application): CoinInfoDao =
            AppDatabase
                .getInstance(application)
                .coinPriceInfoDao()

        @Provides
        @ApplicationScope
        fun provideApiService(): ApiService = ApiFactory.apiService
    }
}
