package com.abadzheva.cryptoapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.abadzheva.cryptoapp.R
import com.abadzheva.cryptoapp.data.model.CoinPriceInfo
import com.abadzheva.cryptoapp.databinding.ItemCoinInfoBinding
import com.squareup.picasso.Picasso

class CoinInfoAdapter(
    private val context: Context,
) : Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {
    lateinit var bindind: ItemCoinInfoBinding
    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener: OnCoinClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CoinInfoViewHolder {
        var binding =
            ItemCoinInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CoinInfoViewHolder(binding)
    }

    override fun getItemCount(): Int = coinInfoList.size

    override fun onBindViewHolder(
        holder: CoinInfoViewHolder,
        position: Int,
    ) {
        val coin = coinInfoList[position]
        with(holder) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbols_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(symbolsTemplate, fromsymbol, tosymbol)
                tvPrice.text = price.toString()
                tvLastUpdate.text = String.format(lastUpdateTemplate, getFormattedTime())
                Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
                itemView.setOnClickListener {
                    onCoinClickListener?.onCoinClick(this)
                }
            }
        }
    }

    inner class CoinInfoViewHolder(
        binding: ItemCoinInfoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val ivLogoCoin = binding.ivLogoCoin
        val tvSymbols = binding.tvSymbols
        val tvPrice = binding.tvPrice
        val tvLastUpdate = binding.tvLastUpdate
    }

    interface OnCoinClickListener {
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}
