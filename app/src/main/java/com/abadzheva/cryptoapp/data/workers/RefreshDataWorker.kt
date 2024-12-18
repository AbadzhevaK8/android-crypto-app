package com.abadzheva.cryptoapp.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.abadzheva.cryptoapp.data.database.CoinInfoDao
import com.abadzheva.cryptoapp.data.mapper.CoinMapper
import com.abadzheva.cryptoapp.data.network.ApiService
import kotlinx.coroutines.delay
import javax.inject.Inject

class RefreshDataWorker(
    context: Context,
    params: WorkerParameters,
    private val coinInfoDao: CoinInfoDao,
    private val apiService: ApiService,
    private val mapper: CoinMapper,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (_: Exception) {
            }
            delay(10000)
        }
    }

    companion object {
        const val NAME = "RefreshDataWorker"

        fun makeRequest(): OneTimeWorkRequest = OneTimeWorkRequestBuilder<RefreshDataWorker>().build()
    }

    class Factory
        @Inject
        constructor(
            private val coinInfoDao: CoinInfoDao,
            private val apiService: ApiService,
            private val mapper: CoinMapper,
        ) : ChildWorkerFactory {
            override fun create(
                context: Context,
                workerParameters: WorkerParameters,
            ): ListenableWorker =
                RefreshDataWorker(
                    context,
                    workerParameters,
                    coinInfoDao,
                    apiService,
                    mapper,
                )
        }
}
