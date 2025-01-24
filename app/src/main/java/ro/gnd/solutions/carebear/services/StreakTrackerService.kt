package ro.gnd.solutions.carebear.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class StreakTrackerService {

    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId ?: "")

    fun updateStreak() {
        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userSnapshot = task.result
                if (userSnapshot.exists()) {
                    val streak = userSnapshot.child("streak").getValue(Int::class.java) ?: 0
                    val lastActiveDate = userSnapshot.child("lastActiveDate").getValue(String::class.java)
                    val today = getCurrentDate()

                    if (today == lastActiveDate) {
                        // Streak already updated for today
                        return@addOnCompleteListener
                    }

                    if (wasYesterday(lastActiveDate)) {
                        // Increment streak
                        userRef.child("streak").setValue(streak + 1)
                    } else {
                        // Reset streak
                        userRef.child("streak").setValue(1)
                    }

                    // Update last active date
                    userRef.child("lastActiveDate").setValue(today)
                }
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun wasYesterday(date: String?): Boolean {
        if (date.isNullOrEmpty()) return false

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DAY_OF_YEAR, -1)

        return try {
            val parsedDate = sdf.parse(date)
            parsedDate != null && sdf.format(parsedDate) == sdf.format(yesterday.time)
        } catch (e: Exception) {
            false
        }
    }
}
