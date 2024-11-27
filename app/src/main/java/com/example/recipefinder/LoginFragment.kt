package com.example.recipefinder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        emailEditText = view.findViewById(R.id.inputEmailLogin)
        passwordEditText = view.findViewById(R.id.InputPasswordLogin)
        loginButton = view.findViewById(R.id.btnLogin)

        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // Automatically navigate to main screen if already logged in
            navigateToMainScreen()
            return
        }


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateInputs(email, password)) {
                loginUser(email, password) { success ->
                    if (success as Boolean) {
                        // Save login state and user details
                        sharedPreferences.edit().apply {
                            putBoolean("isLoggedIn", true)
                            putString("userEmail", email)
                            // Optional: You might want to use more secure storage for passwords
                            // This is just a basic example
                            apply()
                        }
                        // Navigate to main screen
                        navigateToMainScreen()
                    } else {
                        // Handle login failure
                        Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailEditText.error = "Email is required"
            emailEditText.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Please provide a valid email"
            emailEditText.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Password is required"
            passwordEditText.requestFocus()
            return false
        }

        if (password.length < 6) {
            passwordEditText.error = "Password must be at least 6 characters"
            passwordEditText.requestFocus()
            return false
        }

        return true
    }

    private fun navigateToMainScreen() {
        // Replace with your actual navigation logic
        val intent = Intent(activity, AppActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    fun logout() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putBoolean("isLoggedIn", false)
            remove("userEmail")
            apply()
        }

        // Sign out from Firebase or your authentication method
        FirebaseAuth.getInstance().signOut()

        // Navigate back to login screen
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    private fun loginUser(email: String, password: String, onResult: (Boolean) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Toast.makeText(activity, "Login Successful!", Toast.LENGTH_SHORT).show()

                    // Call the result callback
                    onResult(true)

                    // Navigate to another activity
                    val intent = Intent(activity, AppActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    // Login failed
                    Toast.makeText(activity, task.exception?.message, Toast.LENGTH_LONG).show()

                    // Call the result callback
                    onResult(false)
                }
            }
    }
}
