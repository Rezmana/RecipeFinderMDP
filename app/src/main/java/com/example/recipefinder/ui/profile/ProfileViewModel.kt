package com.example.recipefinder.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileViewModel(private val context: Context) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _profileName = MutableLiveData<String>()
    val profileName: LiveData<String> get() = _profileName

    private val _profileEmail = MutableLiveData<String>()
    val profileEmail: LiveData<String> get() = _profileEmail

    private val _profilePictureUrl = MutableLiveData<String>()
    val profilePictureUrl: LiveData<String> get() = _profilePictureUrl

    private val _logoutEvent = MutableLiveData<Boolean>()
    val logoutEvent: LiveData<Boolean> get() = _logoutEvent

    init {
        fetchProfileDetails()
    }

    private fun fetchProfileDetails() {
        val user = auth.currentUser
        if (user != null) {
            _profileEmail.value = user.email

            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        _profileName.value = document.getString("username") ?: "Unknown User"
                        _profilePictureUrl.value = document.getString("profilePicture") // Optional
                    }
                }
                .addOnFailureListener {
                    _profileName.value = "Error fetching name"
                }
        }
    }


    fun logout() {
        // Sign out from Firebase
        auth.signOut()

        // Clear login state in SharedPreferences
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

        // Trigger logout event
        _logoutEvent.value = true
    }

    fun clearLogoutEvent() {
        _logoutEvent.value = false
    }
}
