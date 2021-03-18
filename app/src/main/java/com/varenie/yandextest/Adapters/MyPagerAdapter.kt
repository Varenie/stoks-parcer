package com.varenie.yandextest.Adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.varenie.yandextest.Fragments.PagerFragment

class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    private val titles = arrayOf("Stocks", "Favourite")

    override fun getCount(): Int {
        return titles.size
    }

    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        args.putInt("ARG", position)

        val fragment = PagerFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}