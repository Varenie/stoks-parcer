package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class SearchResponse (
        @SerializedName("symbol")
        var symbol : String,
        @SerializedName("name")
        var name : String,
        @SerializedName("currency")
        var currency : String,
        @SerializedName("stockExchange")
        var stockExchange : String,
        @SerializedName("exchangeShortName")
        var exchangeShortName : String
)