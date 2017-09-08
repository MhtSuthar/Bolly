package com.bolly.gossip.presenter

import android.content.Context
import android.util.Log
import com.my.fire.listner.DashboardListener
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import com.bolly.gossip.model.NewsData
import com.bolly.gossip.utilz.AppUtils
import java.util.ArrayList
import java.util.regex.Pattern


/**
 * Created by mht on 08-Aug-17.
 */
class DashboardPresenter constructor(mDashboardListener: DashboardListener) : Parser.OnTaskCompleted{

    val mDashboardListener: DashboardListener = mDashboardListener
    private val TAG : String = "DashboardPresenter"

    override fun onError() {
        Log.e(TAG, "onError Call")
    }

    override fun onTaskCompleted(list: ArrayList<Article>?) {
        var mListNews : ArrayList<NewsData> = ArrayList()
        if (list != null) {
            for(article in list){
                var imgRegex = "<[iI][mM][gG][^>]+[sS][rR][cC]\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"
                var p = Pattern.compile(imgRegex)
                var m = p.matcher(article.description)
                if (m.find()) {
                    val mNews: NewsData = NewsData(article.title, article.description.split("/>")[1],
                            m.group(1), article.link, article.pubDate)
                    mListNews.add(mNews)
                }
            }
            mDashboardListener.onCompleteCall(mListNews)
        }
    }

    fun getNews(url : String) {
        val urlString = url
        val parser = Parser()
        parser.execute(urlString)
        parser.onFinish(this)
    }

    fun checkAlarmAndSet(context : Context){
        if(!AppUtils.isAlarmOn(AppUtils.ALARM_ID_FOR_NEWS, context)){
            AppUtils.setAlarm(context)
        }
    }



}