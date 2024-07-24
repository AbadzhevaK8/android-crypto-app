package com.abadzheva.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.abadzheva.cryptoapp.api.ApiFactory
import com.abadzheva.cryptoapp.database.AppDatabase
import com.abadzheva.cryptoapp.pojo.CoinPriceInfo
import com.abadzheva.cryptoapp.pojo.CoinPriceInfoRawData
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

    init {
        loadData()
    }

    fun getDetailInfo(fSym: String) = db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)

    private fun loadData() {
        val disposable =
            ApiFactory.apiService
                .getTopCoinsInfo(limit = 50)
                .map {
                    it.data
                        ?.map { it.coinInfo?.name }
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

    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo =
                    Gson().fromJson(
                        currencyJson.getAsJsonObject(currencyKey),
                        CoinPriceInfo::class.java,
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
