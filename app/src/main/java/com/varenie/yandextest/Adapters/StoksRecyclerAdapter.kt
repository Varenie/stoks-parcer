package com.varenie.yandextest.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varenie.yandextest.R
import java.text.FieldPosition

class StoksRecyclerAdapter(private val size: Int): RecyclerView.Adapter<StoksRecyclerAdapter.MyVHolder>() {
    class MyVHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int){}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.recycler_item_stocks, parent, false)

        return MyVHolder(view)
    }

    override fun onBindViewHolder(holder: MyVHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return size
    }


}