package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class Stocks(
        @SerializedName("ticker")
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

