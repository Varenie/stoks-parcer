package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class Result (

	@SerializedName("symbol")
	val symbol : String,
	@SerializedName("name")
	val name : String,
	@SerializedName("exch")
	val exch : String,
	@SerializedName("type")
	val type : String,
	@SerializedName("exchDisp")
	val exchDisp : String,
	@SerializedName("typeDisp")
	val typeDisp : String
)