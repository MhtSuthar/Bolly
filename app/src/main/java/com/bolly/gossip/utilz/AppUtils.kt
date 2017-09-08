package com.bolly.gossip.utilz

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import com.bolly.gossip.R
import android.app.PendingIntent
import com.bolly.gossip.receiver.AlarmReceiver
import android.content.Intent
import android.app.AlarmManager
import android.content.Context.ALARM_SERVICE
import android.util.Log
import java.util.*
import android.util.DisplayMetrics
import android.os.Build
import android.view.*
import android.text.Html
import android.text.Spanned




/**
 * Created by mht on 03-Aug-17.
 */
object  AppUtils {

    val EXTRA_URL: String? = "Url"
    private val TAG = "APpUTils"
    val ALARM_ID_FOR_NEWS = 102

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showSnackBar(context: Context, view: View, text: String) {
        val sb = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        sb.view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        sb.show()
    }

    fun getNavigationbarheight(context: Context) : Int{
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun setAlarm(context: Context) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR_OF_DAY, 4)
        calendar.set(Calendar.MINUTE, 0)

        val alarmMgr = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(context, ALARM_ID_FOR_NEWS, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmMgr?.cancel(alarmIntent)
        alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)

        //SharedPreferenceUtil.putValue(Constants.KEY_IS_ALARM_SETUP, true)
        //SharedPreferenceUtil.save()
        Log.e(TAG, "setAlarm: " + calendar.time)
    }

    fun cancelAlarm(context: Context, alarmId: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, alarmId, intent, 0)
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }

    fun isAlarmOn(mAlarmId: Int, context: Context): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)//the same as up
        val isWorking = PendingIntent.getBroadcast(context, mAlarmId, intent, PendingIntent.FLAG_NO_CREATE) != null//just changed the flag
        Log.e(TAG, "alarm is " + (if (isWorking) "" else "not") + " working...")
        return isWorking
    }

    fun hasSoftKeys(c: Activity): Boolean {
        var hasSoftwareKeys = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d = c.getWindowManager().getDefaultDisplay()

            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)

            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels

            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)

            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels

            hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else {
            val hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }
        return hasSoftwareKeys
    }

    fun fromHtml(source: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            return Html.fromHtml(source)
        }
    }

}