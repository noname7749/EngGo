package com.example.enggo.ui.login

import android.content.Context
import android.content.SharedPreferences
import com.example.enggo.data.service.UserService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert.*

/**
 * Class test cho các chức năng liên quan đến Login.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginLogicTest {

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var firebaseMessaging: FirebaseMessaging

    private lateinit var userService: UserService
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val testUsername = "testUser"
    private val testPassword = "testPass123"
    private val testUserId = "test-user-id"
    private val testFcmToken = "test-fcm-token"

    @Before
    fun setup() {
        userService = UserService(firestore)

        `when`(context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putString(any(), any())).thenReturn(sharedPreferencesEditor)
    }

    /**
     * Test đăng nhập thành công
     */
    @Test
    fun `test login successful`() = runTest {
        // Chuẩn bị dữ liệu test
        `when`(userService.verifyLoginInfo(testUsername, testPassword)).thenReturn(true)
        `when`(userService.getUserIdByUsername(testUsername)).thenReturn(testUserId)

        // Thực thi đăng nhập
        val result = login(testUsername, testPassword, context)

        // Kiểm tra kết quả
        assertTrue(result)
        verify(sharedPreferencesEditor).putString("currentUserId", testUserId)
        verify(sharedPreferencesEditor).putString("currentUsername", testUsername)
        verify(sharedPreferencesEditor).apply()
    }

    /**
     * Test đăng nhập thất bại với thông tin không đúng
     */
    @Test
    fun `test login failed with invalid credentials`() = runTest {
        // Setup mock trả về false cho thông tin đăng nhập sai
        `when`(userService.verifyLoginInfo(testUsername, testPassword)).thenReturn(false)

        // Thực thi đăng nhập
        val result = login(testUsername, testPassword, context)

        // Kiểm tra kết quả
        assertFalse(result)
        verify(sharedPreferencesEditor, never()).putString("currentUserId", any())
    }

    /**
     * Test đăng nhập với username rỗng
     */
    @Test
    fun `test login with empty username`() = runTest {
        val emptyUsername = ""

        val result = login(emptyUsername, testPassword, context)

        assertFalse(result)
    }

    /**
     * Test đăng nhập với password rỗng
     */
    @Test
    fun `test login with empty password`() = runTest {
        val emptyPassword = ""

        val result = login(testUsername, emptyPassword, context)

        assertFalse(result)
    }

    /**
     * Test lưu FCM token sau khi đăng nhập thành công
     */
    @Test
    fun `test FCM token saved after successful login`() = runTest {
        // Setup mock cho đăng nhập thành công
        `when`(userService.verifyLoginInfo(testUsername, testPassword)).thenReturn(true)
        `when`(userService.getUserIdByUsername(testUsername)).thenReturn(testUserId)

        // Thực thi đăng nhập
        login(testUsername, testPassword, context)

        // Verify FCM token được lưu
        verify(userService).addFCMToken(any(), eq(testUserId))
    }

    /**
     * Test session timeout được set khi đăng nhập lần đầu
     */
    @Test
    fun `test session timeout set on first login`() = runTest {
        // Setup mock cho đăng nhập thành công và chưa có session
        `when`(userService.verifyLoginInfo(testUsername, testPassword)).thenReturn(true)
        `when`(userService.getUserIdByUsername(testUsername)).thenReturn(testUserId)
        `when`(sharedPreferences.getString("session", "0")).thenReturn("0")

        // Thực thi đăng nhập
        login(testUsername, testPassword, context)

        // Verify session được set
        verify(sharedPreferencesEditor).putString(eq("session"), any())
    }
}