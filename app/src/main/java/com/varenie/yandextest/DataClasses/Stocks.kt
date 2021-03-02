package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class Stocks(
        @SerializedName("symbol")
        val symbol: String,
        @SerializedName("shortName")
        val name: String,
        @SerializedName("currency")
        val currency: String,
        @SerializedName("preMarketPrice")
        val price: Float,
        @SerializedName("preMarketChange")
        val change: Float,
        @SerializedName("preMarketChangePercent")
        val percent: Float
)
