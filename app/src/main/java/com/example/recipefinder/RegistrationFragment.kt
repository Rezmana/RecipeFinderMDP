package com.example.recipefinder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.example.recipefinder.RegistrationViewModel
import javax.xml.validation.Validator

class RegistrationFragment : Fragment() {
//    private val validator = Validator()
    private val viewModel: RegistrationViewModel by viewModels()

    lateinit var emailInput: EditText
    lateinit var passwordInput: EditText
    lateinit var confirmPasswordInput: EditText
    lateinit var usernameInput: EditText
    private lateinit var continueButton: Button

    lateinit var emailLayout: TextInputLayout
    lateinit var passwordLayout: TextInputLayout
    lateinit var confirmPasswordLayout: TextInputLayout
    lateinit var usernameLayout: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupObservers()
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
            if (validateInputs()) {
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString().trim()
                val username = usernameInput.text.toString().trim()

                viewModel.performRegistration(email, password, username, requireContext())
            }
        }
    }

    private fun setupObservers() {
        viewModel.registrationStatus.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                findNavController().navigate(R.id.fragmentAllergies)
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        })
    }

//    fun validateInputs(): Boolean {
//        val email = emailInput.text.toString().trim()
//        val username = usernameInput.text.toString().trim()
//        val password = passwordInput.text.toString()
//        val confirmPassword = confirmPasswordInput.text.toString()
//
//        val errors = validator.validateInputs(email, username, password, confirmPassword)
//
//        // Reset errors
//        emailLayout.error = null
//        usernameLayout.error = null
//        passwordLayout.error = null
//        confirmPasswordLayout.error = null
//
//        // Apply errors to TextInputLayouts
//        emailLayout.error = errors["email"]
//        usernameLayout.error = errors["username"]
//        passwordLayout.error = errors["password"]
//        confirmPasswordLayout.error = errors["confirmPassword"]
//
//        return errors.isEmpty()
//    }

    fun validateInputs(): Boolean {
        var isValid = true

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

}
