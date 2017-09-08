package com.bolly.gossip.ui.fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bolly.gossip.R
import com.bolly.gossip.model.NewsData
import com.bolly.gossip.ui.NewsDetailActivity
import com.bolly.gossip.utilz.AppUtils
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat


/**
 * Created by mht on 08-Aug-17.
 */
class NewsListFragment(mNewsData: NewsData) : Fragment(){

    lateinit var mImgNews : ImageView
    lateinit var mImgShare : ImageView
    lateinit var mTxtTitle : TextView
    lateinit var mTxtDesc : TextView
    lateinit var mTxtDate : TextView
    lateinit var mRelRoot : RelativeLayout
    lateinit var mRelBottom : RelativeLayout
    val mNewsData: NewsData = mNewsData
    private lateinit var mTxtMore: TextView
    private val TAG : String = "NewsListFragment"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_news_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mImgNews = view!!.findViewById<ImageView>(R.id.img_news)
        mTxtDesc = view!!.findViewById<TextView>(R.id.txt_desc)
        mTxtTitle = view!!.findViewById<TextView>(R.id.txt_title)
        mTxtDate = view!!.findViewById<TextView>(R.id.txt_time)
        mRelRoot = view!!.findViewById<RelativeLayout>(R.id.rel_root)
        mRelBottom = view!!.findViewById<RelativeLayout>(R.id.rel_bottom)
        mImgShare = view!!.findViewById<ImageView>(R.id.img_share)
        mTxtMore = view!!.findViewById<TextView>(R.id.txt_read_more)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && AppUtils.hasSoftKeys(activity)) {
            mImgShare.setPadding(0,0,0, AppUtils.getNavigationbarheight(context))
            mTxtDate.setPadding(0,0,0,AppUtils.getNavigationbarheight(context))
            mTxtMore.setPadding(0,0,0,AppUtils.getNavigationbarheight(context))
        }

        Glide.with(context).load(mNewsData.image).centerCrop().into(mImgNews)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mTxtTitle.text = Html.fromHtml(mNewsData.title, Html.FROM_HTML_MODE_LEGACY)
        } else {
            mTxtTitle.text = Html.fromHtml(mNewsData.title)
        }
        mTxtDesc.text = mNewsData.desc
        val sdf = SimpleDateFormat("dd-MMM-yy")
        mTxtDate.text = sdf.format(mNewsData.pubDate).toString()

        mTxtMore.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(AppUtils.EXTRA_URL, mNewsData.link)
            startActivity(intent)
        }

        mImgShare.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, mNewsData.title+" More read please download our app from play store at: https://play.google.com/store/apps/details?id=com.rajasthan.news")
            sendIntent.type = "text/plain"
            startActivity(sendIntent)
        }
    }
}