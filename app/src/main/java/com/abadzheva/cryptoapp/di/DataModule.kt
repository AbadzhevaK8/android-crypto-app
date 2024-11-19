package com.abadzheva.cryptoapp.di

import android.app.Application
import com.abadzheva.cryptoapp.data.database.AppDatabase
import com.abadzheva.cryptoapp.data.database.CoinInfoDao
import com.abadzheva.cryptoapp.data.repository.CoinRepositoryImpl
import com.abadzheva.cryptoapp.domain.CoinRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    @Binds
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    companion object {
        @Provides
        fun provideCoinInfoDao(application: Application): CoinInfoDao =
            AppDatabase
                .getInstance(application)
                .coinPriceInfoDao()
    }
}