package com.abadzheva.cryptoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.abadzheva.cryptoapp.databinding.FragmentCoinDetailBinding
import com.squareup.picasso.Picasso

class CoinDetailFragment : Fragment() {
    private lateinit var viewModel: CoinViewModel

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentCoinDetailBinding? = null
    private val binding: FragmentCoinDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentCoinDetailBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding =
            FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val fromSymbol = getSymbol()
        viewModel =
            ViewModelProvider(
                this,
            )[CoinViewModel::class.java]
        fromSymbol?.let { it ->
            viewModel.getDetailInfo(it).observe(
                viewLifecycleOwner,
            ) {
                binding.tvPrice.text = it.price.toString()
                binding.tvMinPrice.text = it.lowday.toString()
                binding.tvMaxPrice.text = it.highday.toString()
                binding.tvLastMarket.text = it.lastmarket
                binding.tvLastUpdate.text = it.lastupdate
                binding.tvFromSymbol.text = it.fromsymbol
                binding.tvToSymbol.text = it.tosymbol
                Picasso.get().load(it.imageurl).into(binding.ivLogoCoin)
            }
        }
    }

    private fun getSymbol(): String = requireArguments().getString(EXTRA_FROM_SYMBOL, EMPTY_SYMBOL)

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"
        private const val EMPTY_SYMBOL = ""

        fun newInstance(fromSymbol: String): Fragment =
            CoinDetailFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(EXTRA_FROM_SYMBOL, fromSymbol)
                    }
            }
    }
}
