package com.varenie.yandextest.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.varenie.yandextest.Adapters.NewsRecyclerAdapter
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.DataClasses.News
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.ActivityNewsBinding
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NewsActivity : AppCompatActivity() {
    private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
    private val BASE_URL = "https://financialmodelingprep.com/api/v3"

    lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        binding.rvNews.layoutManager = LinearLayoutManager(this@NewsActivity)
        binding.rvNews.setHasFixedSize(true)

        val intent = intent
        val ticker = intent?.getStringExtra("ticker")

        binding.tvSymbol.text = ticker

        GlobalScope.launch {
            val data = getNews(ticker)
            Log.e("NEWSCHECK", data[0].text)

            runOnUiThread {
                val adapter = NewsRecyclerAdapter(data.size, data)
                binding.rvNews.adapter = adapter
            }
        }
    }

    private suspend fun getNews(ticker: String?): ArrayList<News> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/stock_news?tickers=$ticker&limit=50&apikey=$TOKEN")
            val gson = Gson()

            val stocks = gson.fromJson(response, Array<News>::class.java).toCollection(ArrayList<News>())

            Log.e("NEWSCHECK", stocks[0].toString())
            return stocks
        }
    }
}