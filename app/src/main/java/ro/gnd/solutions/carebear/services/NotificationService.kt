package ro.gnd.solutions.carebear.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.activities.chats.ChatActivity
import ro.gnd.solutions.carebear.activities.friends.FriendRequestsActivity
import ro.gnd.solutions.carebear.models.BaseUser
import ro.gnd.solutions.carebear.models.ChatNotification
import ro.gnd.solutions.carebear.models.FriendRequest
import ro.gnd.solutions.carebear.models.User
import ro.gnd.solutions.carebear.models.UserNotificationHolder
import java.util.Random
import java.util.UUID

class NotificationService private constructor() {
    private val loggedUserId = FirebaseAuth.getInstance().currentUser?.uid
    private val userService = UserService.getInstance()

    private var database = FirebaseDatabase.getInstance()
    private var areNotificationsEnabled = false

    companion object {
        @Volatile
        private var instance: NotificationService? = null

        fun getInstance(): NotificationService {
            return instance ?: synchronized(this) {
                instance ?: NotificationService().also {
                    instance = it
                    it.database = FirebaseDatabase.getInstance()
                    it.loadNotificationPreferences()
                }
            }
        }
    }

    fun sendNotification(
        context: Context,
        title: String,
        message: String,
        pendingIntent: PendingIntent
    ) {
        if (!areNotificationsEnabled) {
            return
        }
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

    fun initChatNotifications(context: Context) {
        val chatNotificationsRef = database.getReference("userNotifications").child(loggedUserId!!)
            .child("chatNotifications")
        chatNotificationsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (chatNotificationSnapshot in dataSnapshot.children) {
                    val chatNotification =
                        chatNotificationSnapshot.getValue(ChatNotification::class.java)
                    chatNotification.let {
                        if (it != null) {
                            if (chatNotification != null) {
                                val intent = Intent(context, ChatActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                }
                                intent.putExtra(ro.gnd.solutions.carebear.fragments.ChatsFragment.CHAT_ID_KEY, chatNotification.chatId)
                                val pendingIntent = PendingIntent.getActivity(
                                    context,
                                    0,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                )
                                sendNotification(
                                    context,
                                    "New message from: " + chatNotification.chatName,
                                    chatNotification.message,
                                    pendingIntent
                                )
                            }
                        }
                    }
                }

                chatNotificationsRef.removeValue()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("Failed to read value: " + databaseError.toException())
            }
        })
    }

    fun sendChatNotification(users: List<BaseUser>, chatNotification: ChatNotification) {
        if (!areNotificationsEnabled) {
            return
        }
        users.forEach { user -> sendChatNotification(user, chatNotification) }
    }

    private fun sendChatNotification(user: BaseUser, chatNotification: ChatNotification) {
        if (!areNotificationsEnabled) {
            return
        }
        val userNotificationsRef = database.getReference("userNotifications")
        userNotificationsRef.child(user.id).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val dataSnapshot = task.result
                if (dataSnapshot.exists()) {
                    val userNotificationHolder = task.result
                        .getValue(UserNotificationHolder::class.java)
                    if (userNotificationHolder != null) {
                        val notifications = userNotificationHolder.chatNotifications.toMutableList()
                        notifications.add(chatNotification)
                        userNotificationHolder.chatNotifications = notifications
                        userNotificationsRef.child(user.id).setValue(userNotificationHolder)
                    }
                } else {
                    val newUserNotification = UserNotificationHolder()
                    newUserNotification.userId = user.id
                    newUserNotification.chatNotifications = listOf(chatNotification)

                    userNotificationsRef.child(user.id).setValue(newUserNotification)
                }
            }
        }
    }

    private fun generateRandomId(): String {
        return UUID.randomUUID().toString()
    }

    private fun loadNotificationPreferences() {
        userService.getLoggedUserReference().addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Convert the snapshot to a User object
                    val loggedUser = snapshot.getValue(
                        User::class.java
                    )
                    if (loggedUser != null) {
                        areNotificationsEnabled = java.lang.Boolean.TRUE
                            .equals(loggedUser.areNotificationsEnabled)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}