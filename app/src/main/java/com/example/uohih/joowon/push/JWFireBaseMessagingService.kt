package com.example.uohih.joowon.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.uohih.joowon.Constants
import com.example.uohih.joowon.R
import com.example.uohih.joowon.ui.intro.IntroActivity
import com.example.uohih.joowon.ui.main.MainListActivity
import com.example.uohih.joowon.util.LogUtil
import com.example.uohih.joowon.util.UICommonUtil
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonArray
import org.json.JSONArray
import java.util.ArrayList

class JWFireBaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().subscribeToTopic(Constants.SUBSCRIBE_BIRTH)
        UICommonUtil.setPreferencesData(Constants.PREFERENCE_FCM_TOKEN, token)
        LogUtil.e("fcmToken:  ", token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val array = remoteMessage.data["data"].toString().split(",")
        val email = UICommonUtil.getPreferencesData(Constants.PREFERENCE_EMAIL)
        for (i in array.indices) {
            if (array[i] == email) {
                showNotification(remoteMessage.data["title"], remoteMessage.data["body"], remoteMessage.data["data"])
                break
            }
        }
    }

    private fun showNotification(title: String?, msg: String?, data: String?) {


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(this, IntroActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("notificationId", data) //전달할 값
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.logo))
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            builder.setSmallIcon(R.drawable.logo)
            val channelName = "BirthChannel"
            val description = "오레오 이상 생일 푸시 채널"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, channelName, importance)
            channel.description = description

            notificationManager.createNotificationChannel(channel)

        } else {
            builder.setSmallIcon(R.drawable.logo)
        }

        notificationManager.notify(0, builder.build())

    }

}