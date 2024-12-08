package com.example.recipefinder

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> get() = _registrationStatus

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun performRegistration(email: String, password: String, username: String, context: Context) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        updateDisplayName(user, username)
                        saveUserToFireStore(user.uid, username, email, context)
                        _registrationStatus.postValue(true)
                    } else {
                        _toastMessage.postValue("User is null")
                        _registrationStatus.postValue(false)
                    }
                } else {
                    _toastMessage.postValue("Registration failed: ${task.exception?.message}")
                    _registrationStatus.postValue(false)
                }
            }
    }

    private fun updateDisplayName(user: FirebaseUser, displayName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(displayName)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseAuth", "User display name updated to: $displayName")
                } else {
                    Log.e("FirebaseAuth", "Failed to update display name: ${task.exception?.message}")
                }
            }
    }

    private fun saveUserToFireStore(userId: String, username: String, email: String, context: Context) {
        val userData = mapOf(
            "username" to username,
            "email" to email
        )

        firestore.collection("users").document(userId)
            .set(userData)
            .addOnSuccessListener {
                saveUserToSharedPreferences(userId, username, email, context)
                _toastMessage.postValue("User profile created!")
            }
            .addOnFailureListener { e ->
                _toastMessage.postValue("Failed to save user data: ${e.message}")
            }
    }

    private fun saveUserToSharedPreferences(userId: String, username: String, email: String, context: Context) {
        val sharedPreferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userId", userId)
        editor.putString("username", username)
        editor.putString("email", email)
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
