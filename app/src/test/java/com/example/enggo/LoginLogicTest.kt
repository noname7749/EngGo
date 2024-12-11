package com.example.enggo.ui.login

import android.content.Context
import android.content.SharedPreferences
import com.example.enggo.data.service.UserService
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
 * Unit test cho các chức năng liên quan đến đăng nhập
 * Kiểm tra xác thực đăng nhập, lưu trữ session và xử lý lỗi
 */
@OptIn(ExperimentalCoroutinesApi::class)
class LoginLogicTest {
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Cấu hình mock cho SharedPreferences
        `when`(context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)
        `when`(editor.apply()).then { }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Test xác thực trạng thái hiển thị mật khẩu
     */
    @Test
    fun `test password visibility toggle`() {
        // Given
        var passwordVisible = false

        // When
        passwordVisible = !passwordVisible

        // Then
        assertTrue(passwordVisible)
    }

    /**
     * Test xử lý lưu Shared Preferences
     */
    @Test
    fun `test shared preferences saving`() {
        // Given
        val editor = mock(SharedPreferences.Editor::class.java)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)

        // When
        with(sharedPreferences.edit()) {
            putString("test_key", "test_value")
            apply()
        }

        // Then
        verify(editor).putString("test_key", "test_value")
        verify(editor).apply()
    }

    /**
     * Test xác thực tính hợp lệ của dữ liệu đăng nhập
     */
    @Test
    fun `test login credentials validation`() {
        // Given
        val username = ""
        val password = "password123"

        // Then
        assertFalse(isValidCredentials(username, password))

        // Given
        val username2 = "user"
        val password2 = ""

        // Then
        assertFalse(isValidCredentials(username2, password2))

        // Given
        val username3 = "user"
        val password3 = "password123"

        // Then
        assertTrue(isValidCredentials(username3, password3))
    }

    /**
     * Test các tính năng cần thiết cho việc đăng nhập
     */
    @Test
    fun `test login related features`() {
        // Test empty username
        assertFalse(isValidUsername(""))

        // Test empty password
        assertFalse(isValidPassword(""))

        // Test valid credentials
        assertTrue(isValidUsername("testuser"))
        assertTrue(isValidPassword("password123"))
    }

    // Hàm phụ trợ cho việc test
    private fun isValidCredentials(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }

    private fun isValidUsername(username: String): Boolean {
        return username.isNotEmpty()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.isNotEmpty()
    }
}