package com.my.fire.listner

import com.bolly.gossip.model.NewsData

/**
 * Created by mht on 03-Aug-17.
 */
interface DashboardListener {

    fun onCompleteCall(response: List<NewsData>)
}