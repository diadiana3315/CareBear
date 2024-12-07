package com.example.carebear.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.carebear.R
import com.google.firebase.database.FirebaseDatabase
import java.util.Random
import java.util.UUID

class NotificationService private constructor() {
    private var database = FirebaseDatabase.getInstance()
    lateinit var notificationManager: NotificationManager

    companion object {
        @Volatile
        private var instance: NotificationService? = null

        fun getInstance(): NotificationService {
            return instance ?: synchronized(this) {
                instance ?: NotificationService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                }
            }
        }
    }

    fun sendNotification(context: Context, title: String, message: String) {
        // Check permissions for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context,
                "Can't send notifications",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val channelId = generateRandomId()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = context.getSystemService(NotificationManager::class.java)

        // Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel.description = "Notification"
            notificationManager?.createNotificationChannel(notificationChannel)
        }

        // Show Notification
        notificationManager?.notify(Random().nextInt(1000000000), builder.build())
    }

    private fun generateRandomId(): String {
        return UUID.randomUUID().toString()
    }
}