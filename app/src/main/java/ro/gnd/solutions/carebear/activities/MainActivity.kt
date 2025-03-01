package ro.gnd.solutions.carebear.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ro.gnd.solutions.carebear.R
import ro.gnd.solutions.carebear.activities.authentications.SsoActivity
import ro.gnd.solutions.carebear.models.User
import ro.gnd.solutions.carebear.services.NotificationService
import ro.gnd.solutions.carebear.services.StreakTrackerService
import ro.gnd.solutions.carebear.services.UserService
import ro.gnd.solutions.carebear.theme.CareBearTheme

class MainActivity : ComponentActivity() {
    private var userService: UserService = UserService.getInstance()
    private var notificationService: NotificationService = NotificationService.getInstance()
    private val activityResultLauncher: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    private lateinit var streakTrackerService: StreakTrackerService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        streakTrackerService = StreakTrackerService()
        streakTrackerService.updateStreak()


        checkIfLoggedIn()

        setContent {
            CareBearTheme {
                // Scaffold provides a basic structure for the app's UI
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }


    }

    override fun onRestart() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            super.onRestart()
        }
    }
//    override fun onResume() {
//        super.onResume()
//
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId ?: "")
//
//        userRef.get().addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val streak = task.result.child("streak").getValue(Int::class.java) ?: 0
//                val tvStreak: TextView = findViewById(R.id.tvStreak)
//                tvStreak.text = "Your Streak: $streak"
//            }
//        }
//    }

    private fun checkIfLoggedIn() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            initialiseNotifications()
            notificationService.initFriendRequestsNotifications(this)
            notificationService.initChatNotifications(this)

            val intent = Intent(this, ro.gnd.solutions.carebear.activities.HomeActivity::class.java)
            this.startActivity(intent)

            if (currentUser.displayName != null && currentUser.email != null) {
                userService.persistUserIfNotPersisted(
                    User(
                        currentUser.uid,
                        currentUser.displayName.toString(),
                        currentUser.email.toString()
                    )
                )
            }
        }
    }

    private fun initialiseNotifications() {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to CareBear!",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Button to navigate to RegisterActivity
        Button(
            onClick = {
                try {
                    val intent = Intent(context, ro.gnd.solutions.carebear.activities.authentications.RegisterActivity::class.java)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error launching RegisterActivity: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(text = "Register")
        }

        Button(
            onClick = {
                val intent = Intent(context, ro.gnd.solutions.carebear.activities.authentications.LoginActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        // Google Authentication Button
        Button(
            onClick = {
                val intent = Intent(context, SsoActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            // Display the Google logo and text
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in with Google",
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    CareBearTheme {
        MainContent()
    }
}
