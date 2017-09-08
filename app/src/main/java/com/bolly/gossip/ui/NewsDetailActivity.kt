package com.bolly.gossip.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.bolly.gossip.R
import com.bolly.gossip.utilz.AppUtils


class NewsDetailActivity : AppCompatActivity() {

    private lateinit var mWebView: WebView
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mProgressBarSide: ProgressBar
    private lateinit var mToolbar: Toolbar
    private var TAG: String = "NewsDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        initView()


        mWebView.settings.setSupportZoom(true)
        mWebView.settings.builtInZoomControls = true
        mWebView.settings.displayZoomControls = true
        mWebView.clearCache(true)
        mWebView.settings.javaScriptEnabled = true
        mWebView.isHorizontalScrollBarEnabled = false
        mWebView.clearHistory()

        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                mProgressBar.progress = progress
                if (progress == 100)
                    mProgressBar.visibility = View.GONE
            }
        }

        mWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mProgressBarSide.visibility = View.GONE
            }
        }

        if(AppUtils.isOnline(this))
            mWebView.loadUrl(intent.extras.getString(AppUtils.EXTRA_URL))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        mWebView = findViewById(R.id.web_view) as WebView
        mProgressBar = findViewById(R.id.progress_bar) as ProgressBar
        mProgressBarSide = findViewById(R.id.progress_bar_side) as ProgressBar
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(mToolbar)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
