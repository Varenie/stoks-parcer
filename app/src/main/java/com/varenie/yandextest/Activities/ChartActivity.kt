package com.varenie.yandextest.Activities

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.varenie.yandextest.DataClasses.TickerHistory
import com.varenie.yandextest.R
import com.varenie.yandextest.ValueFormatter.MyAxisFormatter
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChartActivity : AppCompatActivity() {
    private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
    private val BASE_URL = "https://financialmodelingprep.com/api/v3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val client = HttpClient(CIO)

        val intent = intent
        val ticker = intent?.let {  it.getStringExtra("ticker") }
        // запуск асинхронного потока
        GlobalScope.launch(Dispatchers.IO) {

//            проверка на null
            val history = ticker?.let { getHistory(it) }

            val entries = ArrayList<Entry>()
            val dates = ArrayList<String>()
            val prices = ArrayList<Float>()

            if (history != null) {
                for ((i, x) in history.withIndex()) {
                    entries.add(Entry(i.toFloat(), x.close))
                    dates.add(x.date)
                    prices.add(x.close)
                }
            }

            val max = prices.maxOrNull()
            val min = prices.minOrNull()

            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            val dataSet = LineDataSet(entries, "Акции")
            dataSet.axisDependency = YAxis.AxisDependency.LEFT
            //настройки линии
            dataSet.color = Color.RED
            dataSet.lineWidth = 2f
            dataSet.setCircleColor(Color.RED)

            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_NO -> { // Night mode is not active, we're using the light theme
                    dataSet.valueTextColor = Color.BLACK
                }
                Configuration.UI_MODE_NIGHT_YES -> {// Night mode is active, we're using dark theme
                    dataSet.valueTextColor = Color.WHITE
                }
            }

            val lineData = LineData(dataSet)


            //работа в основном потоке
            runOnUiThread {
                val chart = findViewById<LineChart>(R.id.chart)
                chart.data = lineData


                chart.axisRight.isEnabled = false
                chart.xAxis.valueFormatter = MyAxisFormatter(dates) //отображение дат
                chart.description.isEnabled = false
                chart.setDrawBorders(true)
                chart.invalidate() // refresh

                val leftAxis = chart.axisLeft
                leftAxis.setDrawAxisLine(false)
                leftAxis.setDrawZeroLine(false)
                leftAxis.setDrawGridLines(false)

                val lMax = max?.let { LimitLine(it, "Maximum") }
                lMax?.lineColor = Color.GREEN
                lMax?.lineWidth = 2f

                lMax?.textSize = 12f

                val lMin = min?.let { LimitLine(it, "Minimum") }
                lMin?.lineColor = Color.RED
                lMin?.lineWidth = 2f

                lMin?.textSize = 12f

                when (currentNightMode) {
                    Configuration.UI_MODE_NIGHT_NO -> { // Night mode is not active, we're using the light theme
                        lMax?.textColor = Color.BLACK
                        lMin?.textColor = Color.BLACK
                        chart.description.textColor = Color.BLACK
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {// Night mode is active, we're using dark theme
                        lMax?.textColor = Color.WHITE
                        lMin?.textColor = Color.WHITE
                        chart.legend.textColor = Color.WHITE
                    }
                }

                leftAxis.addLimitLine(lMax)
                leftAxis.addLimitLine(lMin)

            }

        }
    }

    private suspend fun getHistory(ticker: String): Array<TickerHistory> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/historical-chart/4hour/$ticker?apikey=$TOKEN")
            val gson = Gson()

            val history = gson.fromJson(response, Array<TickerHistory>::class.java)

            val entries: ArrayList<Entry> = arrayListOf()

            for ((i, x) in history.withIndex()) {
                entries.add(Entry(i.toFloat(), x.close))
            }
            return history
        }
    }
}