package com.varenie.yandextest.Fragments

import android.os.Bundle
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.DataClasses.DataResponse
import com.varenie.yandextest.R
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PagerFragment: Fragment() {
    private val TOKEN = "KNTxQtvGJzBGimsiX4JT8bcDiyfZsmEmpg2ZaWDi8V1dvl61NOZ53q9wnz6h"
    private val BASE_URL = "https://mboum.com/api/v1"

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

        GlobalScope.launch(Dispatchers.IO) {
            val data = getRequest()

            activity?.runOnUiThread {
                val adapter = StoksRecyclerAdapter(data.count, data.quotes)
                myRecycler.adapter = adapter
            }

        }
        return root
    }

    private suspend fun getRequest(): DataResponse{
        HttpClient().use {
            val response: String = it.get("$BASE_URL/co/collections/?list=most_actives&start=1&apikey=$TOKEN")
            val gson = Gson()

            val data = gson.fromJson(response, DataResponse::class.java)

            return data
        }
    }
}