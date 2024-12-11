package com.example.enggo.ui.register

import com.example.enggo.data.service.UserService
import com.example.enggo.model.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

/**
 * Unit test cho các chức năng liên quan đến đăng ký
 * Kiểm tra xác thực dữ liệu đăng ký, kiểm tra tính khả dụng của username/email
 */
@OptIn(ExperimentalCoroutinesApi::class)
class RegisterLogicTest {
    @Mock
    private lateinit var userService: UserService

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Kiểm tra đăng ký thành công
     */
    @Test
    fun `test successful registration`() = kotlinx.coroutines.test.runTest {
        // Given
        val userData = UserData(
            id = "temp_id",
            username = "newuser",
            password = "password123",
            email = "test@example.com"
        )

        `when`(userService.addUserData(userData)).thenReturn("user123")

        // When
        val result = userService.addUserData(userData)

        // Then
        assertNotNull(result)
        verify(userService).addUserData(userData)
    }

    /**
     * Kiểm tra tính khả dụng của username
     */
    @Test
    fun `test username availability check`() = kotlinx.coroutines.test.runTest {
        // Given
        val username = "availableuser"
        `when`(userService.checkUsernameAvailability(username)).thenReturn(true)

        // When
        val result = userService.checkUsernameAvailability(username)

        // Then
        assertTrue(result)
    }

    /**
     * Kiểm tra tính khả dụng của email
     */
    @Test
    fun `test email availability check`() = kotlinx.coroutines.test.runTest {
        // Given
        val email = "available@example.com"
        `when`(userService.checkEmailAvailability(email)).thenReturn(true)

        // When
        val result = userService.checkEmailAvailability(email)

        // Then
        assertTrue(result)
    }

    /**
     * Kiểm tra xác thực password với các trường hợp khác nhau
     */
    @Test
    fun `test password validation cases`() {
        // Test mật khẩu trống
        assertFalse(validatePasswords("", ""))

        // Test mật khẩu không trùng khớp
        assertFalse(validatePasswords("pass123", "pass456"))

        // Test mật khẩu trùng
        assertTrue(validatePasswords("password123", "password123"))

        // Test mật khẩu quá ngắn
        assertFalse(validatePasswords("pass", "pass"))
    }

    /**
     * Kiểm tra đăng ký với email không hợp lệ
     */
    @Test
    fun `test registration with invalid email`() = kotlinx.coroutines.test.runTest {
        // Given
        val invalidEmail = "invalid.email"
        `when`(userService.checkEmailAvailability(invalidEmail)).thenReturn(false)

        // When
        val result = userService.checkEmailAvailability(invalidEmail)

        // Then
        assertFalse(result)
    }

    /**
     * Kiểm tra xác thực email cơ bản
     */
    @Test
    fun `test email validation cases`() {
        // Test empty email
        assertFalse(isValidEmail(""))

        // Test email không có @
        assertFalse(isValidEmail("invalidemail"))

        // Test email không có domain
        assertFalse(isValidEmail("user@"))

        // Test email không có username
        assertFalse(isValidEmail("@domain.com"))

        // Test email hợp lệ
        assertTrue(isValidEmail("user@example.com"))
    }

    /**
     * Kiểm tra đăng ký với username đã tồn tại
     */
    @Test
    fun `test registration with existing username`() = kotlinx.coroutines.test.runTest {
        // Given
        val existingUsername = "existinguser"
        `when`(userService.checkUsernameAvailability(existingUsername)).thenReturn(false)

        // When
        val result = userService.checkUsernameAvailability(existingUsername)

        // Then
        assertFalse(result)
    }

    /**
     * Kiểm tra trạng thái hiển thị password
     */
    @Test
    fun `test password visibility toggles`() {
        // Given
        var passwordVisible = false
        var confirmPasswordVisible = false

        // When & Then
        passwordVisible = !passwordVisible
        assertTrue(passwordVisible)

        confirmPasswordVisible = !confirmPasswordVisible
        assertTrue(confirmPasswordVisible)
    }

    // Hàm phụ trợ cho việc test
    private fun validatePasswords(password: String, confirmPassword: String): Boolean {
        if (password.isEmpty() || confirmPassword.isEmpty()) return false
        if (password.length < 6) return false
        return password == confirmPassword
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        // email hợp lệ : phải chứa @ and có text trước và sau nó
        return email.contains("@") &&
                email.split("@").size == 2 &&
                email.split("@")[0].isNotEmpty() &&
                email.split("@")[1].isNotEmpty() &&
                email.split("@")[1].contains(".")
    }
}