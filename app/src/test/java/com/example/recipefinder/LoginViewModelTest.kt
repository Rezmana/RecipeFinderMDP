package com.example.recipefinder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule() // For LiveData testing

    private val viewModel = LoginViewModel()

    @Test
    fun `validateInputs should return false for empty email`() {
        val result = viewModel.validateInputs(email = "", password = "password123")
        assertFalse(result)
        assert(viewModel.toastMessage.value == "Email is required")
    }

    @Test
    fun `validateInputs should return false for invalid email`() {
        val result = viewModel.validateInputs(email = "invalid-email", password = "password123")
        assertFalse(result)
        assert(viewModel.toastMessage.value == "Please provide a valid email")
    }

    @Test
    fun `validateInputs should return false for empty password`() {
        val result = viewModel.validateInputs(email = "user@example.com", password = "")
        assertFalse(result)
        assert(viewModel.toastMessage.value == "Password is required")
    }

    @Test
    fun `validateInputs should return false for short password`() {
        val result = viewModel.validateInputs(email = "user@example.com", password = "123")
        assertFalse(result)
        assert(viewModel.toastMessage.value == "Password must be at least 6 characters")
    }

    @Test
    fun `validateInputs should return true for valid inputs`() {
        val result = viewModel.validateInputs(email = "user@example.com", password = "password123")
        assertTrue(result)
    }
}
