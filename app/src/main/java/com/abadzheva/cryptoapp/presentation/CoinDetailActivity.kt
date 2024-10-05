package com.abadzheva.cryptoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.abadzheva.cryptoapp.R
import com.abadzheva.cryptoapp.data.network.ApiFactory.BASE_IMAGE_URL
import com.abadzheva.cryptoapp.databinding.ActivityCoinDetailBinding
import com.abadzheva.cryptoapp.utils.convertTimestampToTime
import com.squareup.picasso.Picasso

class CoinDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_coin_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // ------------------
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // ------------------
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)
        viewModel =
            ViewModelProvider(
                this,
            )[CoinViewModel::class.java]
        fromSymbol?.let { it ->
            viewModel.getDetailInfo(it).observe(
                this,
            ) {
                binding.tvPrice.text = it.price.toString()
                binding.tvMinPrice.text = it.lowday.toString()
                binding.tvMaxPrice.text = it.highday.toString()
                binding.tvLastMarket.text = it.lastmarket
                binding.tvLastUpdate.text = convertTimestampToTime(it.lastupdate)
                binding.tvFromSymbol.text = it.fromsymbol
                binding.tvToSymbol.text = it.tosymbol
                Picasso.get().load(BASE_IMAGE_URL + it.imageurl).into(binding.ivLogoCoin)
            }
        }
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(
            context: Context,
            fromSymbol: String,
        ): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}
