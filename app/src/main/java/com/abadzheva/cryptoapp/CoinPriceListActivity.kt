package com.abadzheva.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.abadzheva.cryptoapp.adapters.CoinInfoAdapter
import com.abadzheva.cryptoapp.databinding.ActivityCoinPriceListBinding
import com.abadzheva.cryptoapp.pojo.CoinPriceInfo

class CoinPriceListActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinPriceListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_coin_price_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ------------------------
        binding = ActivityCoinPriceListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // ------------------------

        val adapter = CoinInfoAdapter(this)
        adapter.onCoinClickListener =
            object : CoinInfoAdapter.OnCoinClickListener {
                override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                    Log.d("MyLog", "onCoinClick: ${coinPriceInfo.fromsymbol}")
                }
            }
        binding.rvCoinPriceList.adapter = adapter

        viewModel =
            ViewModelProvider(
                this,
            )[CoinViewModel::class.java]
        viewModel.priceList.observe(
            this,
            Observer {
                adapter.coinInfoList = it
            },
        )
    }
}
