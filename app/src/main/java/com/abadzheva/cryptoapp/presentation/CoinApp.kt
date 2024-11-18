package com.abadzheva.cryptoapp.presentation

import android.app.Application
import com.abadzheva.cryptoapp.di.DaggerApplicationComponent

class CoinApp : Application() {
    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
