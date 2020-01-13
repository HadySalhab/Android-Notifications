package com.android.notificationchannels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver :BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val extraMessage = it.getStringExtra("toastMessage")
            Toast.makeText(context,extraMessage,Toast.LENGTH_SHORT).show()
        }
    }

}