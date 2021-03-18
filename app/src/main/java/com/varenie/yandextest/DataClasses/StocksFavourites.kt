package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class StocksFavourites(
        @SerializedName("symbol")
        var symbol: String,
        @SerializedName("name")
        var name: String,
        @SerializedName("price")
        var price: Float,
        @SerializedName("change")
        var change: Float,
        @SerializedName("changesPercentage")
        var percent: String
)
