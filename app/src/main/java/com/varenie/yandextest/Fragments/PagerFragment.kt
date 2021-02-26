package com.varenie.yandextest.Fragments

import android.os.Bundle
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.varenie.yandextest.Adapters.StoksRecyclerAdapter
import com.varenie.yandextest.R

class PagerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pager, container, false)

        val myRecycler = root.findViewById<RecyclerView>(R.id.rv_stocks)
        myRecycler.layoutManager = LinearLayoutManager(requireContext())
        myRecycler.setHasFixedSize(true)

        val adapter = StoksRecyclerAdapter(8)
        myRecycler.adapter = adapter

        return root
    }
}