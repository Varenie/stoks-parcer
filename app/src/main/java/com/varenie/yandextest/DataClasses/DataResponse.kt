package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class DataResponse(
        @SerializedName("count")
        val count: Int,
        @SerializedName("quotes")
        val quotes: Array<Stocks>
)
