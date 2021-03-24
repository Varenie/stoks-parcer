package com.varenie.yandextest.DataClasses

import com.google.gson.annotations.SerializedName

data class TickerResponse (
	@SerializedName("ResultSet")
	val resultSet : ResultSet
)