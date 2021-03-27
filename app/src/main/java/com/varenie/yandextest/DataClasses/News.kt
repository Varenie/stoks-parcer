package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("symbol")
    var symbol: String,
    @SerializedName("publishedDate")
    var date: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("image")
    var image: String,
    @SerializedName("site")
    var site: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("url")
    var url: String
)