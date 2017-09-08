package com.bolly.gossip.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.bolly.gossip.model.NewsData

import com.bolly.gossip.ui.fragment.NewsListFragment

class NewsListPagerAdapter(fm: FragmentManager, var mListNews: List<NewsData>) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return mListNews.size
    }

    override fun getItem(position: Int): Fragment {
        return NewsListFragment(mListNews[position])
    }
}