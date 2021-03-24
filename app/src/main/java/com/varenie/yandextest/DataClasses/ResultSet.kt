package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class ResultSet (

	@SerializedName("Query")
	val query : String,
	@SerializedName("Result")
	val result : List<Result>
)