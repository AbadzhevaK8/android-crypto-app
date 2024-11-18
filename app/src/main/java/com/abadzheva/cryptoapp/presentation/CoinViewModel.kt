package com.abadzheva.cryptoapp.presentation

import androidx.lifecycle.ViewModel
import com.abadzheva.cryptoapp.domain.GetCoinInfoListUseCase
import com.abadzheva.cryptoapp.domain.GetCoinInfoUseCase
import com.abadzheva.cryptoapp.domain.LoadDataUseCase
import javax.inject.Inject

class CoinViewModel
    @Inject
    constructor(
        private val getCoinInfoListUseCase: GetCoinInfoListUseCase,
        private val getCoinInfoUseCase: GetCoinInfoUseCase,
        private val loadDataUseCase: LoadDataUseCase,
    ) : ViewModel() {
        val coinInfoList = getCoinInfoListUseCase()

        fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

        init {
            loadDataUseCase()
        }
    }
