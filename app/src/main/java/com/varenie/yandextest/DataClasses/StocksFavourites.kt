package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class StocksFavourites(
        @SerializedName("symbol")
        var symbol: String,
        @SerializedName("companyName")
        var name: String,
        @SerializedName("price")
        var price: Float,
        @SerializedName("changes")
        var change: Float,
        @SerializedName("changesPercentage")
        var percent: String
)
