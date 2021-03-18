package com.varenie.yandextest.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.varenie.yandextest.Activities.ChartActivity
import com.varenie.yandextest.DataBase.TableFavourites
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.RecyclerItemStocksBinding
import java.text.FieldPosition

class StoksRecyclerAdapter(private val size: Int, private val stocks: ArrayList<Stocks>): RecyclerView.Adapter<StoksRecyclerAdapter.MyVHolder>() {
    lateinit var binding: RecyclerItemStocksBinding

    class MyVHolder(private val binding: RecyclerItemStocksBinding): RecyclerView.ViewHolder(binding.root) {
        val context = binding.root.context

//        клик на элемент
        init {
            super.itemView
            itemView.setOnClickListener(View.OnClickListener {

                val intent = Intent(context, ChartActivity::class.java)
                intent.putExtra("ticker", binding.tvStockName.text)
                context.startActivity(intent)
            })
        }

        fun bind(position: Int, stocks: ArrayList<Stocks>){
            binding.tvStockName.text = stocks[position].symbol
            binding.tvCompanyName.text = stocks[position].name
            binding.tvStockCost.text = stocks[position].price.toString()

            if (stocks[position].change < 0)
                binding.tvStockChange.setTextColor(Color.RED)

            val tableFavourites = TableFavourites(context)

            binding.tvStockChange.text = "${stocks[position]?.change} ${stocks[position]?.percent}"

            var isChosen = false
            val ticker = binding.tvStockName.text.toString()

//            Зажиагет звездочку, если акция была в избранном
            if (tableFavourites.isFavourite(ticker)) {
                binding.ivStar.setImageResource(R.drawable.star_chosen)
                isChosen = true

            } else {
                binding.ivStar.setImageResource(R.drawable.star_unchosen)
                isChosen = false

            }

//           клик на звездочку
            binding.ivStar.setOnClickListener{

//                Добавляет или удаляет акцию из избранного
                if (!isChosen) {
                    binding.ivStar.setImageResource(R.drawable.star_chosen)
                    //isChosen = true
                    tableFavourites.addTicker(ticker)
                } else {
                    binding.ivStar.setImageResource(R.drawable.star_unchosen)
                    //isChosen = false
                    tableFavourites.deleteTicker(ticker)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.recycler_item_stocks, parent, false)

        return MyVHolder(binding)
    }

    override fun onBindViewHolder(holder: MyVHolder, position: Int) {
        holder.bind(position, stocks)
    }

    override fun getItemCount(): Int {
        return size
    }


}