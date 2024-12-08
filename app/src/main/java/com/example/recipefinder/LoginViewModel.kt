package com.example.recipefinder

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> get() = _toastMessage

    fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            _toastMessage.value = "Email is required"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _toastMessage.value = "Please provide a valid email"
            return false
        }

        if (password.isEmpty()) {
            _toastMessage.value = "Password is required"
            return false
        }

        if (password.length < 6) {
            _toastMessage.value = "Password must be at least 6 characters"
            return false
        }

        return true
    }

    fun loginUser(email: String, password: String, context: Context) {
        _isLoading.value = true

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    saveUserToSharedPreferences(context, email)
                    _toastMessage.value = "Login Successful!"
                    _loginSuccess.value = true
                } else {
                    _toastMessage.value = task.exception?.message ?: "Login Failed"
                    _loginSuccess.value = false
                }
            }
    }

    private fun saveUserToSharedPreferences(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", true)
            putString("userEmail", email)
            apply()
        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }
}
