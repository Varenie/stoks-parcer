package com.varenie.yandextest.Activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.Gson
import com.varenie.yandextest.DataBase.TableStocks
import com.varenie.yandextest.DataClasses.TickerHistory
import com.varenie.yandextest.R
import com.varenie.yandextest.ValueFormatter.MyAxisFormatter
import com.varenie.yandextest.databinding.ActivityChartBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChartActivity : AppCompatActivity() {
    private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
    private val BASE_URL = "https://financialmodelingprep.com/api/v3"

    lateinit var binding: ActivityChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chart)
//        setContentView(R.layout.activity_chart)

        var isChosen = false
        val intent = intent
        val ticker = intent?.getStringExtra("ticker")

        binding.tvSymbol.text = ticker

        val tableStocks = TableStocks(this)

        if (tableStocks.isFavourite(ticker)){
            binding.ivStar.setImageResource(R.drawable.star_chosen)
            isChosen = true
        }

        binding.ivStar.setOnClickListener {
            if (isChosen) {
                tableStocks.deleteFavourite(ticker)
                binding.ivStar.setImageResource(R.drawable.star_unchosen)
                isChosen = false
            } else {
                val stock = tableStocks.getStock(ticker)
                tableStocks.addFavourite(stock)
                binding.ivStar.setImageResource(R.drawable.star_chosen)
                isChosen = true
            }
        }

        binding.btnNews.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            intent.putExtra("ticker", ticker)
            startActivity(intent)
        }


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

                    //форматирование значения даты, так как оно слишком длинное
                    val date = x.date.substringBefore(" ").substringAfter("-").replace("-", ".")
                    val time = x.date.substringAfter(" ").substringBeforeLast(":")
                    val result = "$date $time"
                    dates.add(result)

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

            // настройки отображения при разных темах телефона
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
                binding.chart.data = lineData

                binding.chart.axisRight.isEnabled = false
                binding.chart.xAxis.valueFormatter = MyAxisFormatter(dates) //отображение дат
                binding.chart.description.isEnabled = false
                binding.chart.setDrawBorders(true)
                binding.chart.invalidate() // refresh
                binding.chart.animateY(1000 , Easing.Linear )

                val leftAxis = binding.chart.axisLeft
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
                        binding.chart.description.textColor = Color.BLACK
                        binding.chart.xAxis.textColor = Color.BLACK
                    }
                    Configuration.UI_MODE_NIGHT_YES -> {// Night mode is active, we're using dark theme
                        lMax?.textColor = Color.WHITE
                        lMin?.textColor = Color.WHITE
                        binding.chart.legend.textColor = Color.WHITE
                        binding.chart.xAxis.textColor = Color.WHITE
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