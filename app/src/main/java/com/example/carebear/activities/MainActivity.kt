package com.example.carebear.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.unit.dp
import com.example.carebear.R
import com.example.carebear.ui.theme.CareBearTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkIfLoggedIn()

        setContent {
            CareBearTheme {
                // Scaffold provides a basic structure for the app's UI
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainContent(modifier = Modifier.padding(innerPadding), signInWithGoogle = { context -> signInWithGoogle(context) })
                }
            }
        }
    }

    override fun onRestart() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            super.onRestart()
        }
    }

    private fun checkIfLoggedIn() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun signInWithGoogle(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)) // Replace with your client ID
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val signInIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Handle the exception
                Log.w("GoogleSignIn", "Google sign-in failed", e)
                Toast.makeText(
                    this,
                    "SignIn with Credential failed: ${task.exception}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        signInIntent.launch(googleSignInClient.signInIntent)
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-in successful
                    val user = firebaseAuth.currentUser
                    Log.d("GoogleSignIn", "SignIn successful: ${user?.displayName}")
                    Toast.makeText(this, "Sign-in successful", Toast.LENGTH_SHORT).show()
                    redirectToHome()
                } else {
                    // If sign in fails
                    Log.w("GoogleSignIn", "SignIn with Credential failed: ${task.exception}")
                    Toast.makeText(
                        this,
                        "SignIn with Credential failed: ${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun redirectToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, signInWithGoogle: (Context) -> Unit) {
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
                    val intent = Intent(context, RegisterActivity::class.java)
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

        // Button to navigate to LoginActivity
        Button(
            onClick = {
                // Start LoginActivity
                val intent = Intent(context, LoginActivity::class.java) // Use context here
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Login")
        }

        // Google Authentication Button
        Button(
            onClick = {
                // Trigger Google Sign-In logic here
                signInWithGoogle(context)
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

//@Preview(showBackground = true)
//@Composable
//fun MainContentPreview() {
//    CareBearTheme {
//        MainContent()
//    }
//}
