package com.varenie.yandextest.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.varenie.yandextest.Adapters.MyPagerAdapter
import com.varenie.yandextest.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById<ViewPager>(R.id.vp_lists_of_stoks)

        val myAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = myAdapter
    }
}