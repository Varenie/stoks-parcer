package com.varenie.yandextest.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.DataBase.TableFavourites
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.DataClasses.TickerHistory
import com.varenie.yandextest.R
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
        val ticker = intent.getStringExtra("ticker")
        // запуск асинхронного потока
        GlobalScope.launch(Dispatchers.IO) {

//            проверка на null
            val entries = ticker?.let {
                getHistory(it)
            }

            val dataSet = LineDataSet(entries, "Label")
            val lineData = LineData(dataSet)
            //работа в основном потоке
            runOnUiThread {
                val chart = findViewById<LineChart>(R.id.chart)
                chart.data = lineData
                chart.invalidate() // refresh

            }

        }
    }

    private suspend fun getHistory(ticker: String): ArrayList<Entry> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/historical-chart/4hour/$ticker?apikey=$TOKEN")
            val gson = Gson()

            val history = gson.fromJson(response, Array<TickerHistory>::class.java)

            val entries: ArrayList<Entry> = arrayListOf()

            for ((i,x) in history.withIndex()) {
                entries.add(Entry(i.toFloat(), x.close))
            }
            return entries
        }
    }
}