package com.abadzheva.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.abadzheva.cryptoapp.data.database.AppDatabase
import com.abadzheva.cryptoapp.data.mapper.CoinMapper
import com.abadzheva.cryptoapp.data.workers.RefreshDataWorker
import com.abadzheva.cryptoapp.domain.CoinInfo
import com.abadzheva.cryptoapp.domain.CoinRepository

class CoinRepositoryImpl(
    private val application: Application,
) : CoinRepository {
    private val mapper = CoinMapper()
    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> =
        coinInfoDao.getPriceList().map { it ->
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> =
        coinInfoDao.getPriceInfoAboutCoin(fromSymbol).map {
            mapper.mapDbModelToEntity(it)
        }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest(),
        )
    }
}
