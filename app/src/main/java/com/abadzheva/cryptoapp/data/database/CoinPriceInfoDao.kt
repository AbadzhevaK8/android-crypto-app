package com.abadzheva.cryptoapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.abadzheva.cryptoapp.data.network.model.CoinInfoDto

@Dao
interface CoinPriceInfoDao {
    @Query("SELECT * FROM full_price_list ORDER BY lastupdate DESC")
    fun getPriceList(): LiveData<List<CoinInfoDto>>

    @Query("SELECT * FROM full_price_list WHERE fromsymbol == :fSym LIMIT 1")
    fun getPriceInfoAboutCoin(fSym: String): LiveData<CoinInfoDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPriceList(priceList: List<CoinInfoDto>)
}
