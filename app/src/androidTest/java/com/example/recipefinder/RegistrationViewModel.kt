import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import com.example.recipefinder.RegistrationViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RegistrationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var testContext: Context

    @Before
    fun setup() {
        viewModel = RegistrationViewModel()
        testContext = ApplicationProvider.getApplicationContext() // Obtain a valid Context
    }

    @Test
    fun `performRegistration with valid inputs updates LiveData successfully`() {
        // Simulate valid registration data
        val email = "test@example.com"
        val password = "password123"
        val username = "TestUser"

        // Observe LiveData
        val registrationObserver = Observer<Boolean> {}
        val toastObserver = Observer<String?> {}
        viewModel.registrationStatus.observeForever(registrationObserver)
        viewModel.toastMessage.observeForever(toastObserver)

        // Simulate registration logic
        viewModel.performRegistration(email, password, username, testContext)

        // Assert LiveData changes
        assertTrue(viewModel.registrationStatus.value ?: false)
        assertEquals("User profile created!", viewModel.toastMessage.value)

        // Clear observers
        viewModel.registrationStatus.removeObserver(registrationObserver)
        viewModel.toastMessage.removeObserver(toastObserver)
    }
}
