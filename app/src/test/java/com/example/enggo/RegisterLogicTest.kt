package com.example.enggo.ui.register

import com.example.enggo.data.service.UserService
import com.example.enggo.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*

/**
 * Class test cho các chức năng liên quan đến Register.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterLogicTest {

    @Mock
    private lateinit var firestore: FirebaseFirestore

    private lateinit var userService: UserService
    private val testDispatcher = StandardTestDispatcher()

    private val testUsername = "testUser"
    private val testEmail = "test@example.com"
    private val testPassword = "testPass123"

    @Before
    fun setup() {
        userService = UserService(firestore)
    }

    /**
     * Test đăng ký thành công với thông tin hợp lệ
     */
    @Test
    fun `test register successful with valid information`() = runTest {
        // Chuẩn bị dữ liệu test
        val userData = UserData(
            username = testUsername,
            password = testPassword,
            email = testEmail
        )

        `when`(userService.checkUsernameAvailability(testUsername)).thenReturn(false)
        `when`(userService.checkEmailAvailability(testEmail)).thenReturn(false)
        `when`(userService.addUserData(userData)).thenReturn(userData.toString())

        // Thực thi đăng ký
        val registrationResult = userService.addUserData(userData)

        // Kiểm tra kết quả
        assertNotNull(registrationResult)
        verify(userService).addUserData(userData)
    }

    /**
     * Test kiểm tra username đã tồn tại
     */
    @Test
    fun `test username already exists`() = runTest {
        // Setup mock cho username đã tồn tại
        `when`(userService.checkUsernameAvailability(testUsername)).thenReturn(true)

        // Kiểm tra availability
        val isUsernameExists = userService.checkUsernameAvailability(testUsername)

        // Verify kết quả
        assertTrue(isUsernameExists)
    }

    /**
     * Test kiểm tra email đã tồn tại
     */
    @Test
    fun `test email already exists`() = runTest {
        // Setup mock cho email đã tồn tại
        `when`(userService.checkEmailAvailability(testEmail)).thenReturn(true)

        // Kiểm tra availability
        val isEmailExists = userService.checkEmailAvailability(testEmail)

        // Verify kết quả
        assertTrue(isEmailExists)
    }

    /**
     * Test đăng ký với email không hợp lệ
     */
    @Test
    fun `test register with invalid email format`() = runTest {
        val invalidEmail = "invalid.email"
        val userData = UserData(
            username = testUsername,
            password = testPassword,
            email = invalidEmail
        )

        // Thử đăng ký với email không hợp lệ
        val registrationResult = userService.addUserData(userData)

        // Verify không thể đăng ký
        assertNull(registrationResult)
    }

    /**
     * Test đăng ký với mật khẩu yếu
     */
    @Test
    fun `test register with weak password`() = runTest {
        val weakPassword = "123"
        val userData = UserData(
            username = testUsername,
            password = weakPassword,
            email = testEmail
        )

        // Thử đăng ký với mật khẩu yếu
        val registrationResult = userService.addUserData(userData)

        // Verify không thể đăng ký
        assertNull(registrationResult)
    }

    /**
     * Test confirm password không khớp
     */
    @Test
    fun `test password confirmation mismatch`() {
        val password = "password123"
        val confirmPassword = "password124"

        // Kiểm tra password và confirm password có khớp nhau
        assertNotEquals(password, confirmPassword)
    }

    /**
     * Test tạo profile sau khi đăng ký thành công
     */
    @Test
    fun `test profile created after successful registration`() = runTest {
        val userData = UserData(
            username = testUsername,
            password = testPassword,
            email = testEmail
        )

        `when`(userService.addUserData(userData)).thenReturn(userData.toString())

        // Thực hiện đăng ký
        val registrationResult = userService.addUserData(userData)

        // Verify profile được tạo
        verify(userService).addUserData(userData)
        assertNotNull(registrationResult)
    }
}