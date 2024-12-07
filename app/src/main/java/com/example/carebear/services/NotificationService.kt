package com.example.carebear.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.carebear.R
import com.example.carebear.activities.friends.FriendRequestsActivity
import com.example.carebear.models.FriendRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Random
import java.util.UUID

class NotificationService private constructor() {
    private var database = FirebaseDatabase.getInstance()
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid

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

    fun sendNotification(context: Context, title: String, message: String, pendingIntent: PendingIntent) {
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
            .setContentIntent(pendingIntent) // Set the PendingIntent
            .setAutoCancel(true)

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

    fun initFriendRequestsNotifications(context: Context) {
        // Create an Intent to redirect the user to a specific Activity
        val intent = Intent(context, FriendRequestsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Add extra data if needed
        intent.putExtra("EXTRA_KEY", "SomeValue")

        // Create a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            context,
            0, // Request code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val friendRequestsRef =
            database.getReference("users").child(loggedUserId!!).child("friendRequests")
        friendRequestsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val friendRequest = userSnapshot.getValue(FriendRequest::class.java)
                    friendRequest.let {
                        if (it != null) {
                            sendNotification(
                                context,
                                "New Friend Request",
                                "You have a new friend request from" + it.requesterEmail,
                                pendingIntent
                            )
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    private fun generateRandomId(): String {
        return UUID.randomUUID().toString()
    }
}