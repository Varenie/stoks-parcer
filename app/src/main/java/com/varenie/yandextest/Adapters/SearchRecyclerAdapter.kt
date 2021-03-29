package com.varenie.yandextest.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.varenie.yandextest.Activities.ChartActivity
import com.varenie.yandextest.DataBase.TableStocks
import com.varenie.yandextest.DataClasses.SearchResponse
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.DataClasses.StocksFavourites
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.RecyclerItemSearchBinding
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchRecyclerAdapter(private val searchResponse: ArrayList<SearchResponse>): RecyclerView.Adapter<SearchRecyclerAdapter.MyVHolder>(){
    lateinit var binding: RecyclerItemSearchBinding

    class MyVHolder(private val binding: RecyclerItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
        private val TOKEN = "24eb674774bb515d57e84b21973fd4cb"
        private val BASE_URL = "https://financialmodelingprep.com/api/v3"

        val context = binding.root.context

        init {
            super.itemView
            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, ChartActivity::class.java)
                intent.putExtra("ticker", binding.tvStockName.text)
                context.startActivity(intent)
            })
        }

        fun bind(position: Int, searchResponse: ArrayList<SearchResponse>) {
            binding.tvStockName.text = searchResponse[position].symbol
            binding.tvCompanyName.text = searchResponse[position].name
            binding.tvCurrency.text = searchResponse[position].currency
            binding.tvStockExchange.text = searchResponse[position].stockExchange

            val tableStocks = TableStocks(context)
            var isChosen = false
            val ticker = binding.tvStockName.text.toString()

//            Зажиагет звездочку, если акция была в избранном
            if (tableStocks.isFavourite(ticker)) {
                binding.ivStar.setImageResource(R.drawable.star_chosen)
                isChosen = true

            } else {
                binding.ivStar.setImageResource(R.drawable.star_unchosen)
                isChosen = false

            }

            binding.ivStar.setOnClickListener {

//                Добавляет или удаляет акцию из избранного
                if (!isChosen) {
                    binding.ivStar.setImageResource(R.drawable.star_chosen)
                    isChosen = true

                    GlobalScope.launch(Dispatchers.IO) {
                        val stocks = getStock(searchResponse[position].symbol)
                        tableStocks.addFavourite(stocks[0])
                    }
                    //tableFavourites.addFavourite(stock)
                } else {
                    binding.ivStar.setImageResource(R.drawable.star_unchosen)
                    isChosen = false
                    tableStocks.deleteFavourite(ticker)
                }
            }
        }

        private suspend fun getStock(symbol: String): ArrayList<Stocks> {
            HttpClient().use {

                val response: String = it.get("$BASE_URL/quote/$symbol?apikey=$TOKEN")
                val gson = Gson()

                val stocks = gson.fromJson(response, Array<StocksFavourites>::class.java)
                val data: ArrayList<Stocks> = arrayListOf()

                for ((i, x) in stocks.withIndex()) {
                    val element = Stocks(x.symbol, x.name, x.price, x.change, x.percent)
                    data.add(element)
                }

                return data
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.recycler_item_search, parent, false)

        return MyVHolder(binding)
    }

    override fun onBindViewHolder(holder: MyVHolder, position: Int) {
        holder.bind(position, searchResponse)
    }

    override fun getItemCount(): Int {
        return searchResponse.size
    }
}