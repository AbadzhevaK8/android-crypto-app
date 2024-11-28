package com.abadzheva.cryptoapp.di

import com.abadzheva.cryptoapp.data.workers.ChildWorkerFactory
import com.abadzheva.cryptoapp.data.workers.RefreshDataWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(RefreshDataWorker::class)
    fun bindRefreshDataWorkerFactory(worker: RefreshDataWorker.Factory): ChildWorkerFactory
}
