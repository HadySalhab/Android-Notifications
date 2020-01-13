package com.android.notificationchannels

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.provider.Telephony
import android.support.v4.media.session.MediaSessionCompat
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media.app.NotificationCompat.MediaStyle
import com.android.notificationchannels.App.Companion.CHANNEL_1_ID
import com.android.notificationchannels.App.Companion.CHANNEL_2_ID

class MainActivity : AppCompatActivity() {
    private lateinit var notificationManager: NotificationManagerCompat
    private lateinit var editTextTile: EditText
    private lateinit var editTextMessage: EditText
    private lateinit var mediaSession: MediaSessionCompat

    companion object {
        val MESSAGES = ArrayList<Messages>()

        fun sendOnNotification(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            val tapIntent = PendingIntent.getActivity(context, 0, intent, 0)

            val remoteInput =
                androidx.core.app.RemoteInput.Builder("key_text_reply").setLabel("Your Answer...")
                    .build()


            var replyIntent: Intent? = null
            var replyPendingIntent: PendingIntent? = null


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                replyIntent = Intent(context, ReplyReceiver::class.java)
                replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, 0)

            }


            val notificationAction = NotificationCompat.Action
                .Builder(R.drawable.ic_skip_previous_black_24dp, "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput).build()


            val messagingStyle = NotificationCompat.MessagingStyle("Me")
            messagingStyle.conversationTitle = "GROUP CHAT"

            MESSAGES.forEach { message ->
                val notificationMessage = NotificationCompat.MessagingStyle.Message(
                    message.text,
                    message.timestamp,
                    message.sender
                )
                messagingStyle.addMessage(notificationMessage)
            }


            val notification = NotificationCompat.Builder(context, CHANNEL_2_ID)
                .setContentIntent(tapIntent)
                .setSmallIcon(R.drawable.ic_two)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setStyle(messagingStyle)
                .setSubText("Sub Text")
                .addAction(notificationAction)
                .setColor(Color.GREEN)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build()
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(5, notification)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)
        editTextTile = findViewById(R.id.edit_text_title)
        editTextMessage = findViewById(R.id.edit_text_message)
        mediaSession = MediaSessionCompat(this, "TAG")
        MESSAGES.apply {
            add(Messages(text = "Good Morning", sender = "Judy"))
            add(Messages(text = "Hello", sender = null))
            add(Messages(text = "Hi", sender = "Milad"))

        }

    }

    fun sendOnChannel1(v: View) {

        val title = editTextTile.text.toString()
        val message = editTextMessage.text.toString()

        val intent = Intent(this, MainActivity::class.java)
        val tapIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val intent2 = Intent(this, NotificationReceiver::class.java)
        intent2.putExtra("toastMessage", message)
        val broadcastIntent =
            PendingIntent.getBroadcast(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT)


        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.profile_picture)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_one) //small icon to the left
            .setContentTitle(title) //title in non-expanded view
            .setContentText(message) //body in non-expanded view
            .setLargeIcon(largeIcon) //right icon in non-expanded view
            .setStyle(
                NotificationCompat.BigTextStyle() //bigtextstyle
                    .bigText(getString(R.string.big_dummy_text)) //body in expanded view
                    .setBigContentTitle("BIG CONTENT TITLE") //title in expanded view
                    .setSummaryText("SUMMARY TEXT") //Set the first line of text after the detail section in the big form of the template.
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH) //same as Channel Importance level
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) //optional
            .setContentIntent(tapIntent) //intent when tapped
            .setColor(Color.BLUE) //color of the notification
            .setAutoCancel(true) //will be cancelled when tapped
            .addAction(R.mipmap.ic_launcher, "Toast", broadcastIntent) //action buttons
            .setOnlyAlertOnce(true) //will alert only the first time the notification is sent
            .build()

        notificationManager.notify(1, notification)


    }

    fun sendOnChannel2(v: View) {
        val title = editTextTile.text.toString()
        val message = editTextMessage.text.toString()
        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_two) //left small icon (twitter = bird)
            .setContentTitle(title) //Notification Title
            .setColor(Color.RED) //RED COLOR
            .setContentText(message) //Notification Body
            .setPriority(NotificationCompat.PRIORITY_LOW) //Same as Channel's Importance level
            .setStyle(
                NotificationCompat.InboxStyle() //inbox Style
                    .addLine("This is Line 1")
                    .addLine("This is Line 2")
                    .addLine("This is Line 3")
                    .addLine("This is Line 4")
                    .addLine("This is Line 5")
                    .addLine("This is Line 6")
                    .setBigContentTitle("BIG CONTENT TITLE") //Expanded view Title
                    .setSummaryText("SUMMARY TEXT") //Set the first line of text after the detail section in the big form of the template.
            )
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()
        notificationManager.notify(2, notification)
    }

    fun sendOnChannel3(v: View) {
        val title = editTextTile.text.toString()
        val message = editTextMessage.text.toString()

        val intent = Intent(this, MainActivity::class.java)
        val tapIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)


        val picture = BitmapFactory.decodeResource(resources, R.drawable.profile_picture)

        val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
            .setSmallIcon(R.drawable.ic_one)
            .setContentTitle(title) //title in non-expanded view
            .setContentText(message) //body in non-expanded view
            .setLargeIcon(picture) //right side icon
            .setStyle(
                NotificationCompat.BigPictureStyle() //big picture Style used
                    .bigPicture(picture) //big picture used
                    .setBigContentTitle("BIG CONTENT TITLE")  //title in expanded view
                    .setSummaryText("SUMMARY TEXT") //Set the first line of text after the detail section in the big form of the template.
                    .bigLargeIcon(null) //on expanded view, the Large icon will disappear
            )

            .setPriority(NotificationCompat.PRIORITY_HIGH) //same as channel's importance level
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) //optional category
            .setContentIntent(tapIntent) //intent after tapping
            .setColor(Color.BLUE) //notification color
            .setAutoCancel(true) //cancelled after tapping
            .setOnlyAlertOnce(true) // will alert only the first time is sent
            .build()

        notificationManager.notify(3, notification)
    }

    fun sendOnChannel4(v: View) {
        val title = editTextTile.text.toString()
        val message = editTextMessage.text.toString()
        val picture = BitmapFactory.decodeResource(resources, R.drawable.profile_picture)
        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_two)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setLargeIcon(picture) //
            .addAction(R.drawable.ic_dislike, "Dislike", null)
            .addAction(R.drawable.ic_skip_previous_black_24dp, "Previous", null)
            .addAction(R.drawable.ic_pause_black_24dp, "Pause", null)
            .addAction(R.drawable.ic_skip_next_black_24dp, "next", null)
            .addAction(R.drawable.ic_like, "Like", null)
            .setStyle(
                MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3)
                    .setMediaSession(mediaSession.sessionToken)

            )
            .setSubText("Sub Text")
            .build()
        notificationManager.notify(4, notification)
    }

    fun sendOnChannel5(v: View) {
        //we are checking if user has disabled the notifications
        if(!notificationManager.areNotificationsEnabled()) {
            //usually we show an alert dialog to explain to the user why u need the notification to be enabled
            //but in this tutorial we will directly open the setting
            openNotificationSettings()
            return
        }

        //we are checking if user of Android Oreo+ has disabled the Channels
        //remember: User < Oreo does not have channel
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O && isChannelBlocked(CHANNEL_2_ID)){
            openChannelSetting(CHANNEL_2_ID)
            return
        }


        sendOnNotification(this)
    }

    private fun openNotificationSettings() {
        //in android 0reo we have a notification setting Activity
      if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
          val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
          intent.putExtra(Settings.EXTRA_APP_PACKAGE,packageName)
          startActivity(intent)
      }
        else {
          //for android<oreo we have a general setting activity
          val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
          intent.setData(Uri.parse("package:$packageName"))
          startActivity(intent)
      }
    }

    @RequiresApi(26)  //this method cannot be called for android < oreo
    //because the user does not have a channel in his device
    //here we are limiting the method accessibility and not a code inside a method
    private fun isChannelBlocked(channelId:String):Boolean{
        //NotificationManagerCompat does not have the method below
        //so we used the normal notification
        val manager = getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
        val channel = manager.getNotificationChannel(channelId)

        //importance NONE means the user has disabled the channel
        return channel!=null && channel.importance.equals(NotificationManager.IMPORTANCE_NONE)
    }
    @RequiresApi(26)
    //user should be >oreo to make sure he has channel
    //this method opens the channel particular channel setting
    private fun openChannelSetting(channelId: String){
        val intent = Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE,packageName)
        intent.putExtra(Settings.EXTRA_CHANNEL_ID,channelId)
        startActivity(intent)
    }


    fun sendOnChannel6(v: View) {
        val progressMax = 100

        val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setContentText("Download in Progress..")
            .setContentTitle("Download")
            .setSmallIcon(R.drawable.ic_two) //left small icon (twitter = bird)
            .setPriority(NotificationCompat.PRIORITY_LOW) //Same as Channel's Importance level
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setProgress(progressMax, 0, false)

        notificationManager.notify(6, notification.build())

        Thread(Runnable {
            SystemClock.sleep(2000)
            for (x in (0 until progressMax) step 10) {
                notification.setProgress(progressMax, x, false)
                notificationManager.notify(6, notification.build())
                SystemClock.sleep(1000)
            }
            notification.setContentText("Download Finished")
                .setProgress(0, 0, false)
                .setOngoing(false)
            notificationManager.notify(6, notification.build())
        }).start()
    }

    fun sendOnChannel7(v: View) {

        val title1 = "Title 1"
        val message1 = "Message 1"
        val title2 = "Title 2"
        val message2 = "Message 2"

        val notification1 = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setContentText(message1)
            .setContentTitle(title1)
            .setSmallIcon(R.drawable.ic_two) //left small icon (twitter = bird)
            .setPriority(NotificationCompat.PRIORITY_LOW) //Same as Channel's Importance level
            .setGroup("example_group")
            .build()

        val notification2 = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setContentText(message2)
            .setContentTitle(title2)
            .setSmallIcon(R.drawable.ic_two) //left small icon (twitter = bird)
            .setPriority(NotificationCompat.PRIORITY_LOW) //Same as Channel's Importance level
            .setGroup("example_group")
            .build()




        //summary notification, to support grouping  behavior for android <7
        //for android>7 where grouping is a default behavior,we can still use summaryNotification because
        // this still gives us control over the default grouping behavior
        val summaryNotification = NotificationCompat.Builder(this, CHANNEL_2_ID)
            .setSmallIcon(R.drawable.ic_skip_previous_black_24dp) //left small icon (twitter = bird)
            //style here is optional, but it is convenient to use inboxStyle to look bit similar to notification
            // group for android >7 (Nougat) in which grouping is supported by default
            .setStyle(NotificationCompat.InboxStyle()
                .addLine("$title2 $message2")
                .addLine("$title1 $message1")
                .setSummaryText("user@example.com")
            )
            .setPriority(NotificationCompat.PRIORITY_LOW) //Same as Channel's Importance level
            .setGroup("example_group")
            .setGroupSummary(true)
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .build()



        SystemClock.sleep(2000)
        notificationManager.notify(7, notification1)
        SystemClock.sleep(2000)
        notificationManager.notify(8,notification2)
        SystemClock.sleep(2000)
        notificationManager.notify(9,summaryNotification)


    }
}
