package com.abadzheva.cryptoapp.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.abadzheva.cryptoapp.domain.CoinInfo

object CoinInfoDiffCallback : DiffUtil.ItemCallback<CoinInfo>() {
    override fun areItemsTheSame(
        oldItem: CoinInfo,
        newItem: CoinInfo,
    ): Boolean = oldItem.fromsymbol == newItem.fromsymbol

    override fun areContentsTheSame(
        oldItem: CoinInfo,
        newItem: CoinInfo,
    ): Boolean = oldItem == newItem
}
