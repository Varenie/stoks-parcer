package com.varenie.yandextest.Adapters

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.varenie.yandextest.Activities.ChartActivity
import com.varenie.yandextest.DataBase.TableStocks
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.RecyclerItemStocksBinding
import java.math.RoundingMode
import java.text.DecimalFormat


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
            binding.tvStockCost.text = "\$ ${stocks[position].price}"

            val percent = stocks[position].percent
            val percentFormat = percent.substringBefore("%").substringAfter("+").substringAfter("-")// api возвращает проценты либо в формате (+3.12%), либо 0.07000000. Для этогт приводим к общему виду

            //для округления слишком длинных значений
            val percentNum = percentFormat.toFloat()
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING

            if (stocks[position].change < 0) {
                binding.tvStockChange.setTextColor(Color.RED)
                binding.tvStockChange.text = "${stocks[position]?.change} (-${df.format(percentNum)}%)"
            } else {
                binding.tvStockChange.text ="${stocks[position]?.change} (+${df.format(percentNum)}%)"
            }

            val tableFavourites = TableStocks(context)
            val stock = stocks[position]

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
                    tableFavourites.addFavourite(stock)
                } else {
                    binding.ivStar.setImageResource(R.drawable.star_unchosen)
                    //isChosen = false
                    tableFavourites.deleteFavourite(ticker)
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