package com.android.notificationchannels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput

class ReplyReceiver :BroadcastReceiver (){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val remoteInput = RemoteInput.getResultsFromIntent(it)
            val replyText = remoteInput.getCharSequence("key_text_reply")
            val answer = Messages(text = replyText!!,sender = null)
            MainActivity.MESSAGES.add(answer)
            MainActivity.sendOnNotification(context!!)
        }
    }

}