package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName
import java.util.*

data class TickerHistory(
    @SerializedName("date")
    val date: String,
    @SerializedName("close")
    val close: Float
)
