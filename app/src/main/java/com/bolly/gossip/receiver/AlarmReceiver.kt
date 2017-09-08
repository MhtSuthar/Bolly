package com.bolly.gossip.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import com.bolly.gossip.R
import com.bolly.gossip.model.NewsData
import com.bolly.gossip.utilz.AppUtils
import com.bolly.gossip.utilz.NotificationUtils
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Created by mht on 11-Aug-17.
 */
class AlarmReceiver : BroadcastReceiver(), Parser.OnTaskCompleted{

    lateinit var mContext : Context

    override fun onReceive(context: Context?, p1: Intent?) {
        mContext = context!!
        if(AppUtils.isOnline(context!!)){
            getNews(context.getString(R.string.bollywood_feed))
        }
        AppUtils.cancelAlarm(context!!, AppUtils.ALARM_ID_FOR_NEWS)
        AppUtils.setAlarm(context)
    }

    fun getNews(url : String) {
        val urlString = url
        val parser = Parser()
        parser.execute(urlString)
        parser.onFinish(this)
    }

    override fun onError() {

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
            if(mListNews.size > 2){
                NotificationUtils().showBigNotification(mListNews[0].image, mListNews[0].title, mContext)
                NotificationUtils().showBigNotification(mListNews[1].image, mListNews[1].title, mContext)
            }
        }
    }

}
