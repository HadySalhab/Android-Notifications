package com.android.notificationchannels

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class App : Application() {
    companion object {
        val GROUP_1_ID = "group1"
        val GROUP_2_ID = "group2"

        //belong to group 1
        val CHANNEL_1_ID = "channel1"
        val CHANNEL_2_ID = "channel2"

        //belong to group 2
        val CHANNEL_3_ID = "channel3"

        //does not belong to group
        val CHANNEL_4_ID = "channel4"


    }

    override fun onCreate() {
        super.onCreate()

        createNotificationChannels()

    }

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            //Creating the groups
            val group1 = NotificationChannelGroup(GROUP_1_ID, "Group 1")
            val group2 = NotificationChannelGroup(GROUP_2_ID, "Group 2")


            //Creating channel 1
            val channel1 =
                NotificationChannel(CHANNEL_1_ID, "Channel1 ", NotificationManager.IMPORTANCE_HIGH)
            channel1.description = "this is channel 1"
            channel1.group = GROUP_1_ID


            //creating channel2
            val channel2 =
                NotificationChannel(CHANNEL_2_ID, "Channel2 ", NotificationManager.IMPORTANCE_LOW)
            channel2.description = "this is channel 2"
            channel2.group = GROUP_1_ID


            //creating channel3
            val channel3 =
                NotificationChannel(CHANNEL_3_ID, "Channel3 ", NotificationManager.IMPORTANCE_HIGH)
            channel3.description = "this is channel 3"
            channel3.group = GROUP_2_ID

            //creating channel4
            val channel4 =
                NotificationChannel(CHANNEL_4_ID, "Channel4 ", NotificationManager.IMPORTANCE_LOW)
            channel4.description = "this is channel 4"


            //assigning Channels and ChannelGroups to NotificationManager
            val manager = getSystemService((Context.NOTIFICATION_SERVICE)) as NotificationManager
            manager.apply {
                createNotificationChannelGroup(group1)
                createNotificationChannelGroup(group2)
                createNotificationChannel(channel1)
                createNotificationChannel(channel2)
                createNotificationChannel(channel3)
                createNotificationChannel(channel4)

            }


        }
    }
}