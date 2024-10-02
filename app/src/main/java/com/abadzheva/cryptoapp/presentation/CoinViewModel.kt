package com.abadzheva.cryptoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.abadzheva.cryptoapp.data.database.AppDatabase
import com.abadzheva.cryptoapp.data.network.ApiFactory
import com.abadzheva.cryptoapp.data.network.model.CoinInfoDto
import com.abadzheva.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private var db = AppDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun getDetailInfo(fSym: String) = db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)

    init {
        loadData()
    }

    private fun loadData() {
        val disposable =
            ApiFactory.apiService
                .getTopCoinsInfo(limit = 50)
                .map {
                    it.names
                        ?.map { it.coinName?.name }
                        ?.joinToString(",")
                        .toString()
                }.flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it) }
                .map { getPriceListFromRawData(it) }
                .delaySubscription(10, TimeUnit.SECONDS)
                .repeat()
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.coinPriceInfoDao().insertPriceList(it)
                    Log.d("TEST_OF_LOADING_DATA", "Success: $it")
                }, {
                    Log.d("TEST_OF_LOADING_DATA", "Failure: ${it.message}")
                })
    }

    private fun getPriceListFromRawData(coinInfoJsonContainerDto: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = ArrayList<CoinInfoDto>()
        val jsonObject = coinInfoJsonContainerDto.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo =
                    Gson().fromJson(
                        currencyJson.getAsJsonObject(currencyKey),
                        CoinInfoDto::class.java,
                    )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
