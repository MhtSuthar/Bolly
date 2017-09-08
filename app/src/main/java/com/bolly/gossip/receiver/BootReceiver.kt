package com.bolly.gossip.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.bolly.gossip.utilz.AppUtils


/**
 * Created by mht on 11-Aug-17.
 */
class BootReceiver : BroadcastReceiver(){

    override fun onReceive(p0: Context?, p1: Intent?) {
        AppUtils.setAlarm(p0!!)
    }

}