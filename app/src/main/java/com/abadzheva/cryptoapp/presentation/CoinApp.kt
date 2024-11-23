package com.abadzheva.cryptoapp.presentation

import android.app.Application
import androidx.work.Configuration
import com.abadzheva.cryptoapp.data.database.AppDatabase
import com.abadzheva.cryptoapp.data.mapper.CoinMapper
import com.abadzheva.cryptoapp.data.network.ApiFactory
import com.abadzheva.cryptoapp.data.workers.RefreshDataWorkerFactory
import com.abadzheva.cryptoapp.di.DaggerApplicationComponent

class CoinApp :
    Application(),
    Configuration.Provider {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(
                    RefreshDataWorkerFactory(
                        AppDatabase.getInstance(this).coinPriceInfoDao(),
                        ApiFactory.apiService,
                        CoinMapper(),
                    ),
                ).build()
}
