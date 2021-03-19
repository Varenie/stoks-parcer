package com.varenie.yandextest.ValueFormatter

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class MyAxisFormatter(dates: ArrayList<String>) : ValueFormatter() { //класс для верхнего лэйбла на графике
    val dates = dates
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return dates.getOrNull(value.toInt()) ?: value.toString()
    }
}