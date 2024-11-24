package com.abadzheva.cryptoapp.presentation

import android.app.Application
import androidx.work.Configuration
import com.abadzheva.cryptoapp.data.workers.RefreshDataWorkerFactory
import com.abadzheva.cryptoapp.di.DaggerApplicationComponent
import javax.inject.Inject

class CoinApp :
    Application(),
    Configuration.Provider {
    @Inject
    lateinit var workerFactory: RefreshDataWorkerFactory

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
        super.onCreate()
    }

    override val workManagerConfiguration: Configuration
        get() =
            Configuration
                .Builder()
                .setWorkerFactory(workerFactory)
                .build()
}
