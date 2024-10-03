package com.abadzheva.cryptoapp.data.mapper

import com.abadzheva.cryptoapp.data.database.CoinInfoDbModel
import com.abadzheva.cryptoapp.data.network.model.CoinInfoDto
import com.abadzheva.cryptoapp.data.network.model.CoinInfoJsonContainerDto
import com.abadzheva.cryptoapp.data.network.model.CoinNamesListDto
import com.abadzheva.cryptoapp.domain.CoinInfo
import com.google.gson.Gson

class CoinMapper {
    fun mapDtoToDbModel(dto: CoinInfoDto): CoinInfoDbModel =
        CoinInfoDbModel(
            fromsymbol = dto.fromsymbol,
            tosymbol = dto.tosymbol,
            price = dto.price,
            lastupdate = dto.lastupdate,
            highday = dto.highday,
            lowday = dto.lowday,
            lastmarket = dto.lastmarket,
            imageurl = dto.getFullImageUrl(),
        )

    fun mapJsonContainerToListCoinInfo(jsonContainer: CoinInfoJsonContainerDto): List<CoinInfoDto> {
        val result = mutableListOf<CoinInfoDto>()
        val jsonObject = jsonContainer.json ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo =
                    Gson().fromJson(
                        currencyJson.getAsJsonObject(currencyKey),
                        CoinInfoDto::class.java,
                    )
                result.add(priceInfo)
            }
        }
        return result
    }

    fun mapNamesListToString(namesListDto: CoinNamesListDto): String =
        namesListDto.names
            ?.map { it.coinName?.name }
            ?.joinToString(",")
            .toString() ?: ""

    fun mapDbModelToEntity(dbModel: CoinInfoDbModel) =
        CoinInfo(
            fromsymbol = dbModel.fromsymbol,
            tosymbol = dbModel.tosymbol,
            price = dbModel.price,
            lastupdate = dbModel.lastupdate,
            highday = dbModel.highday,
            lowday = dbModel.lowday,
            lastmarket = dbModel.lastmarket,
            imageurl = dbModel.imageurl,
        )
}
