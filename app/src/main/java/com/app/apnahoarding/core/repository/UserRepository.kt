package com.app.apnahoarding.core.repository



import com.app.apnahoarding.ui.screens.signup.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun isUserProfileComplete(): Boolean {
        val user = auth.currentUser ?: return false


        val document = db.collection("users").document(user.uid).get().await()

        if (!document.exists()) return false

        val fullName = document.getString("fullName")
        val mobile = document.getString("mobileNumber")
        val state = document.getString("state")
        val userType = document.getString("userType")

        return !fullName.isNullOrBlank() &&
                !mobile.isNullOrBlank() &&
                !state.isNullOrBlank() &&
                !userType.isNullOrBlank()

    }


    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null;
    }


    fun registerUser(user: User, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onError(Exception("User not logged in"))
        db.collection("users")
            .document(uid)
            .set(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

}
