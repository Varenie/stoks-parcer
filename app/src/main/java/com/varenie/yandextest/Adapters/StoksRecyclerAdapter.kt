package com.varenie.yandextest.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.varenie.yandextest.DataClasses.Stocks
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.RecyclerItemStocksBinding
import java.text.FieldPosition

class StoksRecyclerAdapter(private val size: Int, private val stocks: Array<Stocks>): RecyclerView.Adapter<StoksRecyclerAdapter.MyVHolder>() {
    lateinit var binding: RecyclerItemStocksBinding

    class MyVHolder(private val binding: RecyclerItemStocksBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, stocks: Array<Stocks>){
            binding.tvStockName.text = stocks[position].symbol
            binding.tvCompanyName.text = stocks[position].name
            binding.tvStockCost.text = stocks[position].price.toString()

            if (stocks[position].change < 0)
                binding.tvStockChange.setTextColor(Color.RED)

            binding.tvStockChange.text = "${stocks[position].change} (${stocks[position].percent}%)"
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