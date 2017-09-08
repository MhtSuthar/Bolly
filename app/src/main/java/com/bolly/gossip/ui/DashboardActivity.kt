package com.bolly.gossip.ui

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bolly.gossip.R
import com.bolly.gossip.model.NewsData
import com.bolly.gossip.presenter.DashboardPresenter
import com.bolly.gossip.ui.adapter.NewsListPagerAdapter
import com.bolly.gossip.utilz.AppUtils
import com.bolly.gossip.utilz.RateItDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import com.my.fire.listner.DashboardListener
import me.kaelaela.verticalviewpager.VerticalViewPager
import me.kaelaela.verticalviewpager.transforms.StackTransformer


class DashboardActivity : AppCompatActivity(), DashboardListener {

    private lateinit var mViewpager: VerticalViewPager
    private lateinit var mAdapter: NewsListPagerAdapter
    private lateinit var mImgLoader: ImageView
    private lateinit var mDashboardPresenter: DashboardPresenter
    private lateinit var mRelHeader: RelativeLayout
    private lateinit var mAddView: AdView
    private lateinit var mInterstitialAd: InterstitialAd
    private var mFullAddDisplayed: Boolean = false
    private val TAG: String = "DashboardActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDashboardPresenter = DashboardPresenter(this)
        mDashboardPresenter.checkAlarmAndSet(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        initView()

        initAds()

        showLoader()

        if (AppUtils.isOnline(this))
            mDashboardPresenter.getNews(getString(R.string.bollywood_feed))
        else
            AppUtils.showSnackBar(this, mViewpager, getString(R.string.no_internet))

    }

    private fun initAds() {
        val adRequest = AdRequest.Builder()
                .build()

        mAddView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)

        // set the ad unit ID
        mInterstitialAd.adUnitId = getString(R.string.interstitial_full_screen)

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest)

        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {

            }
        }
    }

    private fun showLoader() {
        mImgLoader.visibility = View.VISIBLE
        val imageViewTarget = GlideDrawableImageViewTarget(mImgLoader)
        Glide.with(this).load(R.raw.loading).into(imageViewTarget)
    }

    private fun hideLoader() {
        mImgLoader.visibility = View.GONE
    }

    private fun setViewpagerAdapter(response: List<NewsData>) {
        mAdapter = NewsListPagerAdapter(supportFragmentManager, response)
        mViewpager.adapter = mAdapter
        mViewpager.setPageTransformer(true, StackTransformer())
    }

    private fun initView() {
        mViewpager = findViewById(R.id.view_pager) as VerticalViewPager
        mImgLoader = findViewById(R.id.img_loader) as ImageView
        mAddView = findViewById(R.id.adView) as AdView
        mRelHeader = findViewById(R.id.rel_header) as RelativeLayout


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var mHeight: Int = AppUtils.getStatusBarHeight(this)
            mRelHeader.setPadding(0, mHeight, 0, 0)
            if (AppUtils.hasSoftKeys(this)) {
                //mImgMore.setPadding(mHeight, mHeight, mHeight / 2, AppUtils.getNavigationbarheight(this) + 20)
            }
        }

        /**
         * Showing Rating Dialog
         */
        RateItDialogFragment.show(this, supportFragmentManager)
    }

    override fun onCompleteCall(response: List<NewsData>) {
        mImgLoader.visibility = View.GONE
        setViewpagerAdapter(response)
    }

    override fun onBackPressed() {
        if (mInterstitialAd.isLoaded && !mFullAddDisplayed) {
            mInterstitialAd.show()
            mFullAddDisplayed = true
        } else
            super.onBackPressed()
    }

}
