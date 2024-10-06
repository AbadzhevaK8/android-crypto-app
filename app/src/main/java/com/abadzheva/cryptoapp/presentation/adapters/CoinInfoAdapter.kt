package com.abadzheva.cryptoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.abadzheva.cryptoapp.R
import com.abadzheva.cryptoapp.databinding.ItemCoinInfoBinding
import com.abadzheva.cryptoapp.domain.CoinInfo
import com.squareup.picasso.Picasso

class CoinInfoAdapter(
    private val context: Context,
) : Adapter<CoinInfoViewHolder>() {
    var coinInfoList: List<CoinInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CoinInfoViewHolder {
        val binding =
            ItemCoinInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )

        return CoinInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = coinInfoList.size

    override fun onBindViewHolder(
        holder: CoinInfoViewHolder,
        position: Int,
    ) {
        val coin = coinInfoList[position]
        with(holder.binding) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(symbolsTemplate, fromsymbol, tosymbol)
                tvPrice.text = price.toString()
                tvLastUpdate.text =
                    String.format(lastUpdateTemplate, lastupdate)
                Picasso.get().load(imageurl).into(ivLogoCoin)
                root.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinInfo)
    }
}
