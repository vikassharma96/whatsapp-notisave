package com.example.notidemo

import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.text.Html
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MainActivity : AppCompatActivity() {
    var customer:CustomResultReceiver? = null
    var textview: TextView? = null;

    val onNotice : Lazy<BroadcastReceiver> = lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val pack = intent!!.getStringExtra("package")
                val title = intent.getStringExtra("title")
                val text = intent.getStringExtra("text")
                runOnUiThread(object : Runnable {
                    override fun run() {
                        textview!!.setTextColor(Color.parseColor("#0B0719"))
                        textview!!.text = Html.fromHtml("$pack<br><b>$title : </b>$text")
                    }
                })

            }
        }
    }

    companion object{
        private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"

        private const val ACTION_NOTIFICATION_LISTENER_SETTINGS =
            "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textview = findViewById<TextView>(R.id.textView)

    }


    override fun onResume() {
        super.onResume()
        if (!isNotificationServiceEnabled()){
            startActivity(Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
        }else{
            LocalBroadcastManager.getInstance(this).registerReceiver(onNotice.value, IntentFilter("Msg"));
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat: String = Settings.Secure.getString(
            contentResolver,
            ENABLED_NOTIFICATION_LISTENERS
        )
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}