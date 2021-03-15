package com.varenie.yandextest.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.DataBase.TableFavourites
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.DataClasses.StocksFavourites
import com.varenie.yandextest.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PagerFragment: Fragment() {
    private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
    private val BASE_URL = "https://financialmodelingprep.com/api/v3"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pager, container, false)


        val myRecycler = root.findViewById<RecyclerView>(R.id.rv_stocks)
        myRecycler.layoutManager = LinearLayoutManager(requireContext())
        myRecycler.setHasFixedSize(true)

        val client = HttpClient(CIO)
        val arguments = arguments
        var pos: String = ""

        arguments?.let {
            pos = it.getString("position").toString()
        }

        // запуск асинхронного потока
        GlobalScope.launch(Dispatchers.IO) {
            var data: Array<Stocks?>
            if (pos.compareTo("1") == 0) {
                val tableFavourites = TableFavourites(requireContext())
                val tickers = tableFavourites.getTickers()

                data = arrayOfNulls(0)

                if (tickers.compareTo("") != 0) {
                    data = getFavouriteRequest(tickers)
                }
            } else {
                data = getRequest()

            }

            //работа в основном потоке
            activity?.runOnUiThread {
                val adapter = StoksRecyclerAdapter(data.size, data)
                myRecycler.adapter = adapter
            }

        }
        return root

    }

    private suspend fun getFavouriteRequest(tickers: String): Array<Stocks?> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/quote/$tickers?apikey=$TOKEN")
            val gson = Gson()

            val stocks = gson.fromJson(response, Array<StocksFavourites>::class.java)
            var data = arrayOfNulls<Stocks>(stocks.size)

            for ((i, x) in stocks.withIndex()){
                data[i]?.symbol = x.symbol
                data[i]?.name = x.name
                data[i]?.price = x.price
                data[i]?.change = x.change
                data[i]?.percent = x.percent
            }

            return data
        }
    }

    private suspend fun getRequest(): Array<Stocks?> {
        HttpClient().use {
            val response: String = it.get("$BASE_URL/actives?apikey=$TOKEN")
            val gson = Gson()

            val stocks = gson.fromJson(response, Array<Stocks?>::class.java)

           return stocks
        }
    }
}