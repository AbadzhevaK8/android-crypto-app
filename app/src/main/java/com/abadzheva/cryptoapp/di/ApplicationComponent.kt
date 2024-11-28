package com.abadzheva.cryptoapp.di

import android.app.Application
import com.abadzheva.cryptoapp.presentation.CoinApp
import com.abadzheva.cryptoapp.presentation.CoinDetailFragment
import com.abadzheva.cryptoapp.presentation.CoinPriceListActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class,
    ],
)
interface ApplicationComponent {
    fun inject(activity: CoinPriceListActivity)

    fun inject(fragment: CoinDetailFragment)

    fun inject(application: CoinApp)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): ApplicationComponent
    }
}
