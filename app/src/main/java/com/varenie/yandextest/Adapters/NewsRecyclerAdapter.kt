package com.varenie.yandextest.Adapters

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.varenie.yandextest.DataClasses.News
import com.varenie.yandextest.R
import com.varenie.yandextest.databinding.RecyclerItemNewsBinding


class NewsRecyclerAdapter(private val size: Int, private val news: ArrayList<News>): RecyclerView.Adapter<NewsRecyclerAdapter.MyVHolder>() {
    lateinit var binding: RecyclerItemNewsBinding

    class MyVHolder(private val binding: RecyclerItemNewsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, news: ArrayList<News>){
            binding.tvSite.text = news[position].site
            binding.tvTitle.text = news[position].title
            binding.tvDate.text = news[position].date
            //binding.tvUrl.text = news[position].url
            binding.tvText.text = news[position].text

            Log.e("NEWSCHECK", "${news[position].text}")

            Picasso.get()
                    .load(news[position].image)
                    .resize(100, 100)
                    .centerCrop()
                    .into(binding.ivNewsLogo)

            binding.tvUrl.setOnClickListener {
                val context = binding.root.context
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news[position].url))
                context.startActivity(browserIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.recycler_item_news, parent, false)

        return MyVHolder(binding)
    }

    override fun onBindViewHolder(holder: MyVHolder, position: Int) {
        holder.bind(position, news)
    }

    override fun getItemCount(): Int {
        return size
    }


}