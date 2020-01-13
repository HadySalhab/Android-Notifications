package com.android.notificationchannels

class Messages(
     var text: CharSequence,
     var timestamp: Long = System.currentTimeMillis(),
     var sender: CharSequence?
) {

}