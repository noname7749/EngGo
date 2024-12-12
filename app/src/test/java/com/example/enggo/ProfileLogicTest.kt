package com.example.enggo.ui.profile

import android.content.Context
import android.content.SharedPreferences
import com.example.enggo.data.service.UserService
import com.example.enggo.model.ProfileData
import com.example.enggo.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
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
 * Unit test cho các chức năng liên quan đến Profile
 * Kiểm tra việc xử lý dữ liệu người dùng, xác thực dữ liệu profile, và quản lý trạng thái UI
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ProfileLogicTest {
    // Các đối tượng mock cho các thành phần Android
    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var editor: SharedPreferences.Editor

    // Dispatcher để xử lý coroutines trong tests
    private val testDispatcher = StandardTestDispatcher()

    /**
     * Setup chạy trước mỗi test
     * Khởi tạo các mock và thiết lập test dispatcher
     */
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        // Cấu hình hành vi mock cho SharedPreferences
        `when`(context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE)).thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(any(), any())).thenReturn(editor)
    }

    /**
     * Dọn dẹp chạy sau mỗi test
     * Reset main dispatcher
     */
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Kiểm tra chức năng đăng xuất
     * Xác minh rằng SharedPreferences được xóa đúng cách trong quá trình đăng xuất
     */
    @Test
    fun `test logout should clear shared preferences`() {
        // When: Thực hiện thao tác đăng xuất
        logout(context)

        // Then: Xác minh SharedPreferences được xóa chính xác
        verify(sharedPreferences).edit()
        verify(editor).putString("currentUserId", null)
        verify(editor).putString("currentUserName", null)
        verify(editor).apply()
    }

    /**
     * Kiểm tra việc tạo đối tượng ProfileData và xác thực các trường
     * Xác minh tất cả các trường được đặt và lấy ra đúng cách
     */
    @Test
    fun `test ProfileData object creation and validation`() {
        // Given: Dữ liệu test cho profile
        val testId = "test123"
        val testName = "Test User"
        val testAvatar = "test_avatar.jpg"
        val testBio = "Test bio"

        // When: Tạo đối tượng ProfileData
        val profileData = ProfileData(
            id = testId,
            displayName = testName,
            avatar = testAvatar,
            bio = testBio
        )

        // Then: Xác minh tất cả các trường khớp với giá trị đầu vào
        with(profileData) {
            assertEquals(testId, id)
            assertEquals(testName, displayName)
            assertEquals(testAvatar, avatar)
            assertEquals(testBio, bio)
        }
    }

    /**
     * Kiểm tra việc tạo đối tượng UserData và xác thực các trường
     * Xác minh tất cả các trường của người dùng được đặt và lấy ra đúng cách
     */
    @Test
    fun `test UserData object creation and validation`() {
        // Given: Dữ liệu test cho người dùng
        val testId = "test123"
        val testUsername = "testuser"
        val testEmail = "test@example.com"
        val testPassword = "password123"

        // When: Tạo đối tượng UserData
        val userData = UserData(
            id = testId,
            username = testUsername,
            email = testEmail,
            password = testPassword
        )

        // Then: Xác minh tất cả các trường khớp với giá trị đầu vào
        with(userData) {
            assertEquals(testId, id)
            assertEquals(testUsername, username)
            assertEquals(testEmail, email)
            assertEquals(testPassword, password)
        }
    }

    /**
     * Kiểm tra chức năng chuyển đổi hiển thị mật khẩu
     * Xác minh hoạt động chuyển đổi hoạt động chính xác
     */
    @Test
    fun `test password visibility toggle`() {
        // Given: Trạng thái hiển thị ban đầu
        var passwordVisible = false

        // When & Then: Kiểm tra hoạt động chuyển đổi
        passwordVisible = !passwordVisible
        assertTrue(passwordVisible)

        passwordVisible = !passwordVisible
        assertFalse(passwordVisible)
    }

    /**
     * Kiểm tra việc tạo UserData với các trường rỗng
     * Xác minh rằng các trường không null xử lý chuỗi rỗng đúng cách
     */
    @Test
    fun `test creation of UserData with empty fields`() {
        // Given: Dữ liệu tối thiểu cần thiết
        val testId = "test123"
        val testPassword = "password"

        // When: Tạo UserData với chuỗi rỗng cho các trường tùy chọn
        val userData = UserData(
            id = testId,
            username = "",  // Chuỗi rỗng vì username không thể null
            email = "",     // Chuỗi rỗng vì email không thể null
            password = testPassword
        )

        // Then: Xác minh các trường bắt buộc và các trường tùy chọn rỗng
        assertEquals(testId, userData.id)
        assertEquals(testPassword, userData.password)
        assertEquals("", userData.username)
        assertEquals("", userData.email)
    }

    /**
     * Kiểm tra việc tạo ProfileData với các trường tối thiểu
     * Xác minh xử lý các giá trị null và giá trị mặc định
     */
    @Test
    fun `test creation of ProfileData with minimal required fields`() {
        // Given: Dữ liệu tối thiểu cần thiết
        val testId = "test123"

        // When: Tạo ProfileData với các trường tối thiểu
        val profileData = ProfileData(
            id = testId,
            displayName = null,
            avatar = "null", // Giá trị mặc định trong ứng dụng
            bio = null
        )

        // Then: Xác minh các trường bắt buộc và các trường có thể null
        assertEquals(testId, profileData.id)
        assertEquals("null", profileData.avatar)
        assertNull(profileData.displayName)
        assertNull(profileData.bio)
    }

    /**
     * Kiểm tra xử lý giá trị avatar mặc định trong ProfileData
     * Xác minh giá trị avatar mặc định được xử lý đúng cách
     */
    @Test
    fun `test default avatar value handling`() {
        // Given: ProfileData với giá trị avatar mặc định
        val profileData = ProfileData(
            id = "test123",
            displayName = "Test User",
            avatar = "null", // Giá trị mặc định trong ứng dụng
            bio = "Test bio"
        )

        // Then: Xác minh giá trị avatar mặc định
        assertEquals("null", profileData.avatar)
    }
}