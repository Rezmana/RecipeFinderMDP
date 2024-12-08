package com.example.recipefinder

import android.content.Intent
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
import com.example.recipefinder.LoginViewModel

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupObservers()
        setupListeners()
    }

    private fun initializeViews(view: View) {
        emailEditText = view.findViewById(R.id.inputEmailLogin)
        passwordEditText = view.findViewById(R.id.InputPasswordLogin)
        loginButton = view.findViewById(R.id.btnLogin)
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            loginButton.isEnabled = !isLoading
        })

        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                // Navigate to AppActivity
                val intent = Intent(activity, AppActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearToastMessage()
            }
        })
    }

    private fun setupListeners() {
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (viewModel.validateInputs(email, password)) {
                viewModel.loginUser(email, password, requireContext())
            }
        }
    }
}
