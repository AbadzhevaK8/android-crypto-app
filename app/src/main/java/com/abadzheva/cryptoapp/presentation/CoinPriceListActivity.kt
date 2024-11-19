package com.abadzheva.cryptoapp.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.abadzheva.cryptoapp.R
import com.abadzheva.cryptoapp.databinding.ActivityCoinPriceListBinding
import com.abadzheva.cryptoapp.domain.CoinInfo
import com.abadzheva.cryptoapp.presentation.adapters.CoinInfoAdapter
import javax.inject.Inject

class CoinPriceListActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: ActivityCoinPriceListBinding

    private val component by lazy {
        (application as CoinApp).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
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
                override fun onCoinClick(coinPriceInfo: CoinInfo) {
                    isOnePaneMode(coinPriceInfo)
                }
            }
        binding.rvCoinPriceList.adapter = adapter
        binding.rvCoinPriceList.itemAnimator = null

        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory,
            )[CoinViewModel::class.java]
        viewModel.coinInfoList.observe(
            this,
        ) {
            adapter.submitList(it)
        }
    }

    private fun isOnePaneMode(coinPriceInfo: CoinInfo) {
        if (binding.fragmentContainer == null) {
            launchDetailActivity(coinPriceInfo.fromsymbol)
        } else {
            launchDetailFragment(coinPriceInfo.fromsymbol)
        }
    }

    private fun launchDetailActivity(fromSymbol: String) {
        val intent =
            CoinDetailActivity.newIntent(
                this@CoinPriceListActivity,
                fromSymbol,
            )
        startActivity(intent)
    }

    private fun launchDetailFragment(fromSymbol: String) {
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, CoinDetailFragment.newInstance(fromSymbol))
            .addToBackStack(null)
            .commit()
    }
}
