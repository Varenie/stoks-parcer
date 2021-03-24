package com.varenie.yandextest.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.DataBase.TableStocks
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.DataClasses.StocksFavourites
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.ActivityMainBinding
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
    private val BASE_URL = "https://financialmodelingprep.com/api/v3"

    private val ID_STOCkS_PAGE = 0
    private val ID_FAVOURITES_PAGE = 1
    private val ID_SEARCH_PAGE = 2

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rvStocks.layoutManager = LinearLayoutManager(this)
        binding.rvStocks.setHasFixedSize(true)

        GlobalScope.launch {

            while (true) {
                updateStocks()
                delay(1_600_000)  // обновление каждые 30 минут
            }
        }

        loadStocks(ID_STOCkS_PAGE)

        binding.btnFavourites.setOnClickListener {
            loadStocks(ID_FAVOURITES_PAGE)
        }

        binding.btnStocks.setOnClickListener {
            loadStocks(ID_STOCkS_PAGE)
        }

        binding.searchView.inputType = InputType.TYPE_CLASS_NUMBER;
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("TESTSEACRH", "onQueryTextSubmit: $query")

                if (query != null) {
                    val upperQuery = query.toUpperCase()
                    loadStocks(ID_SEARCH_PAGE, upperQuery)
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("TESTSEACRH", "onQueryTextChange: $newText")
                return false
            }
        })

    }

    private fun updateStocks() {
        val tableStocks = TableStocks(this)

        GlobalScope.launch(Dispatchers.IO) {
            val defaultStocks = getDefaultRequest()
            Log.e("COURUTINESTEST", defaultStocks[0].toString())

            tableStocks.cleanCashe()    //очищаем кэш, так как наиболее активные тикеры могут измениться
            tableStocks.addCashe(defaultStocks) //загружаем в таблицу новые значения

            val tickers = tableStocks.getTickers()  //берем избранных тикеров, для обновления информации о них
            val favouriteStocks = getSearchRequest(tickers)

            for(stock in  favouriteStocks) { // обновляем данные для каждого тикера
                tableStocks.updateFavourite(stock)
            }
        }
    }

    private fun loadStocks(idPage: Int, query: String = "") {

        // запуск асинхронного потока
        GlobalScope.launch(Dispatchers.IO) {

            val tableStocks = TableStocks(this@MainActivity)
            var data = ArrayList<Stocks>()

            when(idPage){
                ID_STOCkS_PAGE -> {
                    data = tableStocks.getCasheStocks()
                }

                ID_FAVOURITES_PAGE -> {

                    data  = tableStocks.getFavourites()
                }

                ID_SEARCH_PAGE -> {
                    data = getSearchRequest(query)
                }

            }


            //работа в основном потоке
            runOnUiThread {
                val adapter = StoksRecyclerAdapter(data.size, data)
                binding.rvStocks.adapter = adapter
            }

        }
    }


    private suspend fun getSearchRequest(tickers: String): ArrayList<Stocks> {

        HttpClient().use {
            if (tickers.compareTo("") != 0) {
                val response: String = it.get("$BASE_URL/quote/$tickers?apikey=$TOKEN")
                val gson = Gson()

                val stocks = gson.fromJson(response, Array<StocksFavourites>::class.java)
                val data: ArrayList<Stocks> = arrayListOf()

                for ((i, x) in stocks.withIndex()) {
                    val element = Stocks(x.symbol, x.name, x.price, x.change, x.percent)
                    data.add(element)
                }

                return data
            } else {
                return arrayListOf()
            }
        }
    }

    private suspend fun getDefaultRequest(): ArrayList<Stocks> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/actives?apikey=$TOKEN")
            val gson = Gson()

            val stocks = gson.fromJson(response, Array<Stocks>::class.java)

            val data: ArrayList<Stocks> = arrayListOf()

            for (x in stocks) {
                data.add(x)
            }
            return data
        }
    }
}