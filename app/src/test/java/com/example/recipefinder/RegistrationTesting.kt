package com.example.recipefinder

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.example.recipefinder.RegistrationFragment
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class RegistrationFragmentTest {

    private lateinit var fragment: RegistrationFragment
    private val emailInput: EditText = mockk(relaxed = true)
    private val usernameInput: EditText = mockk(relaxed = true)
    private val passwordInput: EditText = mockk(relaxed = true)
    private val confirmPasswordInput: EditText = mockk(relaxed = true)
    private val emailLayout: TextInputLayout = mockk(relaxed = true)
    private val usernameLayout: TextInputLayout = mockk(relaxed = true)
    private val passwordLayout: TextInputLayout = mockk(relaxed = true)
    private val confirmPasswordLayout: TextInputLayout = mockk(relaxed = true)

    @Before
    fun setUp() {
        fragment = spyk(RegistrationFragment())

        // Inject mocks into the fragment
        every { fragment.emailInput } returns emailInput
        every { fragment.usernameInput } returns usernameInput
        every { fragment.passwordInput } returns passwordInput
        every { fragment.confirmPasswordInput } returns confirmPasswordInput

        every { fragment.emailLayout } returns emailLayout
        every { fragment.usernameLayout } returns usernameLayout
        every { fragment.passwordLayout } returns passwordLayout
        every { fragment.confirmPasswordLayout } returns confirmPasswordLayout
    }

    @Test
    fun `validateInputs should return true for valid inputs`() {
        // Arrange
        every { emailInput.text.toString() } returns "user@example.com"
        every { usernameInput.text.toString() } returns "username123"
        every { passwordInput.text.toString() } returns "password123"
        every { confirmPasswordInput.text.toString() } returns "password123"

        // Act
        val result = fragment.validateInputs()

        // Assert
        assertTrue(result)
        verify { emailLayout.error = null }
        verify { usernameLayout.error = null }
        verify { passwordLayout.error = null }
        verify { confirmPasswordLayout.error = null }
    }

    @Test
    fun `validateInputs should return false for empty fields`() {
        // Arrange
        every { emailInput.text.toString() } returns ""
        every { usernameInput.text.toString() } returns ""
        every { passwordInput.text.toString() } returns ""
        every { confirmPasswordInput.text.toString() } returns ""

        // Act
        val result = fragment.validateInputs()

        // Assert
        assertFalse(result)
        verify { emailLayout.error = "Email is required" }
        verify { usernameLayout.error = "Username is required" }
        verify { passwordLayout.error = "Password is required" }
        verify { confirmPasswordLayout.error = "Please confirm your password" }
    }

    @Test
    fun `validateInputs should return false for mismatched passwords`() {
        // Arrange
        every { emailInput.text.toString() } returns "user@example.com"
        every { usernameInput.text.toString() } returns "username123"
        every { passwordInput.text.toString() } returns "password123"
        every { confirmPasswordInput.text.toString() } returns "password321"

        // Act
        val result = fragment.validateInputs()

        // Assert
        assertFalse(result)
        verify { confirmPasswordLayout.error = "Passwords do not match" }
    }
}