package com.example.notidemo

import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class NotiService : NotificationListenerService()
{



    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val msgrcv = Intent("Msg")
        msgrcv.putExtra("ticker", "ticker")
        msgrcv.putExtra("title", "title")
        msgrcv.putExtra("text", "text")

        sbn.packageName?.let {
             msgrcv.putExtra("package", it)
        }

        sbn.notification.tickerText?.let {
            msgrcv.putExtra("ticker", it.toString())
        }

        sbn.notification.extras?.let {extras->
            extras.getString("android.title")?.let {
                msgrcv.putExtra("title", it)
            }
             extras.getCharSequence("android.text")?.toString()?.let {
                 msgrcv.putExtra("text", it)
             }
        }
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        Log.i("Msg", "Notification Removed")
    }
}