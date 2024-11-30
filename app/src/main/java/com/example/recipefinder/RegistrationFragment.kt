package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class RegistrationFragment : Fragment() {
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var usernameInput: EditText
    private lateinit var continueButton: Button

    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var usernameLayout: TextInputLayout


    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth

    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,

    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()



        // Initialize views
        initializeViews(view)
        setupListeners()


    }

    private fun initializeViews(view: View) {
        usernameInput = view.findViewById(R.id.usernameTextInput)
        emailInput = view.findViewById(R.id.emailRegistrationInput)
        passwordInput = view.findViewById(R.id.passwordTextInputRegistration)
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput)


        usernameLayout = view.findViewById(R.id.usernameInputLayout)
        emailLayout = view.findViewById(R.id.emailInputLayout)
        passwordLayout = view.findViewById(R.id.passwordInputLayoutRegistration)
        confirmPasswordLayout = view.findViewById(R.id.confirmPasswordInputLayout)
        continueButton = view.findViewById(R.id.continueButton)
    }

    private fun setupListeners() {
        continueButton.setOnClickListener {
            performRegistration()
            // TODO: DONT FORGET TO UNCOMMENT THE VALIDATION INPUT
//            if (validateInputs()) {
//                performRegistration()
//            }
        }
    }

    private fun validateInputs(): Boolean {

        var isValid = true

        // Reset all errors
        emailLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null
        usernameLayout.error = null

        val email = emailInput.text.toString().trim()
        if (email.isEmpty()) {
            emailLayout.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.error = "Invalid email format"
            isValid = false
        }

        val username = usernameInput.text.toString().trim()
        if (username.isEmpty()) {
            usernameLayout.error = "Username is required"
            isValid = false
        } else if (username.length < 3) {
            usernameLayout.error = "Username must be at least 3 characters"
            isValid = false
        }

        val password = passwordInput.text.toString()
        if (password.isEmpty()) {
            passwordLayout.error = "Password is required"
            isValid = false
        } else if (password.length < 8) {
            passwordLayout.error = "Password must be at least 8 characters"
            isValid = false
        }

        val confirmPassword = confirmPasswordInput.text.toString()
        if (confirmPassword.isEmpty()) {
            confirmPasswordLayout.error = "Please confirm your password"
            isValid = false
        } else if (confirmPassword != password) {
            confirmPasswordLayout.error = "Passwords do not match"
            isValid = false
        }

        return isValid
    }


    private fun performRegistration() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        val username = usernameInput.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        saveUsernameToFirestore(user.uid, username)
//                        Toast.makeText(context,"Registration Successful", Toast.LENGTH_SHORT).show();
                        updateUI(user)
                    } else {
                        Toast.makeText(context, "User is null", Toast.LENGTH_SHORT).show()

                    }
                } else {
                    Toast.makeText(
                        context, "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun saveUsernameToFirestore(userId: String, username: String) {
        val userData = mapOf(
            "username" to username,
            "email" to emailInput.text.toString().trim()
        )

        firestore.collection("users").document(userId)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(context, "User profile created!", Toast.LENGTH_SHORT).show()
                updateUI(auth.currentUser)
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            Toast.makeText(context, "Registration successful!", Toast.LENGTH_LONG).show()
            // Navigate to the next screen, e.g.:
             findNavController().navigate(R.id.fragmentAllergies)
        }
    }
}
