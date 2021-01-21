package com.jumpitt.happ.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jumpitt.happ.ui.main.MainActivity
import androidx.core.app.NotificationCompat
import com.jumpitt.happ.R
import com.jumpitt.happ.ble.BleManagerImpl
import com.jumpitt.happ.ui.splash.SplashActivity
import org.json.JSONObject


@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("Borrar", "Notificacion 00")
        if(remoteMessage.data.isNotEmpty()){
            showNotification(remoteMessage.data["title"], remoteMessage.data["text"])
            Log.e("Borrar", "DATA: "+ (remoteMessage.data["data"]))
//            val answer = JSONObject(remoteMessage.data["data"])
//            Log.e("Borrar", "COD TYPE: "+ answer["cod_type"])
        }

        remoteMessage.notification?.let { notification ->
            showNotification(notification.title, notification.body)
        }
    }

    private fun showNotification(title: String?, message: String?){
        Log.e("Borrar", "Notificacion 01")
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(this,"happ_notification")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel("happ_notification", "notification", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(88, builder.build())
    }

}