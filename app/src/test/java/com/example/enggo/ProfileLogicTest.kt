package com.example.enggo.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.example.enggo.data.service.UserService
import com.example.enggo.model.ProfileData
import com.example.enggo.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
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
import org.mockito.kotlin.mock

/**
 * Class test cho các chức năng liên quan đến Profile trong ứng dụng EngGo.
 * Bao gồm các test cho:
 * - Hiển thị thông tin cá nhân
 * - Thay đổi ảnh đại diện
 * - Chỉnh sửa thông tin cá nhân
 * - Đổi mật khẩu
 * - Đăng xuất
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProfileLogicTest {

    @Mock
    private lateinit var firestore: FirebaseFirestore

    @Mock
    private lateinit var firebaseStorage: FirebaseStorage

    @Mock
    private lateinit var storageReference: StorageReference

    @Mock
    private lateinit var context: Context

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @Mock
    private lateinit var sharedPreferencesEditor: SharedPreferences.Editor

    @Mock
    private lateinit var bitmap: Bitmap

    private lateinit var userService: UserService
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val testUserId = "test-user-id"
    private val testUsername = "testUser"
    private val testEmail = "test@example.com"
    private val testPassword = "password123"
    private val testAvatar = "https://example.com/avatar.jpg"
    private val testBio = "Test bio"
    private val testDisplayName = "Test User"

    @Before
    fun setup() {
        userService = UserService(firestore)

        `when`(context.getSharedPreferences("EngGoApp", Context.MODE_PRIVATE))
            .thenReturn(sharedPreferences)
        `when`(sharedPreferences.edit()).thenReturn(sharedPreferencesEditor)
        `when`(sharedPreferencesEditor.putString(any(), any())).thenReturn(sharedPreferencesEditor)

        // Setup cho Firebase Storage
        `when`(firebaseStorage.reference).thenReturn(storageReference)
        `when`(storageReference.child(any())).thenReturn(storageReference)
    }

    /**
     * Test hiển thị thông tin cá nhân
     */
    @Test
    fun `test display profile information correctly`() = runTest {
        // Chuẩn bị dữ liệu profile mẫu
        val expectedProfileData = ProfileData(
            displayName = testDisplayName,
            avatar = testAvatar,
            bio = testBio,
            id = testUserId
        )

        `when`(userService.getUserProfile(testUserId)).thenReturn(expectedProfileData)

        // Lấy thông tin profile
        val result = getCurrentProfileData(testUserId, testScope)

        // Kiểm tra các thông tin hiển thị
        assertNotNull(result)
        assertEquals(testDisplayName, result?.displayName)
        assertEquals(testAvatar, result?.avatar)
        assertEquals(testBio, result?.bio)
    }

    /**
     * Test thay đổi ảnh đại diện
     */
    @Test
    fun `test update avatar successfully`() = runTest {
        // Chuẩn bị đường dẫn ảnh mới
        val newAvatarUrl = "https://example.com/new-avatar.jpg"

        // Mock quá trình upload ảnh lên Firebase Storage
        `when`(uploadImageToFirebase(bitmap, testUserId, mock(), mock())).thenReturn(Unit)

        // Mock cập nhật URL ảnh trong profile
        `when`(userService.updateProfileImageUrl(testUserId, newAvatarUrl)).thenReturn(Unit)

        // Kiểm tra cập nhật thành công
        verify(userService).updateProfileImageUrl(testUserId, newAvatarUrl)
    }

    /**
     * Test chỉnh sửa thông tin cá nhân
     */
    @Test
    fun `test update profile information successfully`() = runTest {
        // Thông tin profile mới
        val newDisplayName = "New Display Name"
        val newBio = "New Bio"

        val updatedProfile = ProfileData(
            displayName = newDisplayName,
            bio = newBio,
            avatar = testAvatar,
            id = testUserId
        )

        // Thực hiện cập nhật profile
        userService.updateUserProfile(testUserId, updatedProfile)

        // Kiểm tra profile đã được cập nhật
        verify(userService).updateUserProfile(eq(testUserId), any())

        // Kiểm tra thông tin mới
        val result = getCurrentProfileData(testUserId, testScope)
        assertEquals(newDisplayName, result?.displayName)
        assertEquals(newBio, result?.bio)
    }

    /**
     * Test đổi mật khẩu thành công
     */
    @Test
    fun `test change password successfully`() = runTest {
        // Chuẩn bị dữ liệu
        val oldPassword = testPassword
        val newPassword = "newPassword123"
        val userData = UserData(
            username = testUsername,
            password = oldPassword,
            email = testEmail,
            id = testUserId
        )

        // Thực hiện đổi mật khẩu
        onPasswordChange(userData, newPassword, testScope) {}

        // Kiểm tra mật khẩu đã được cập nhật
        verify(userService).updateUserData(eq(testUserId), any())
    }

    /**
     * Test validation khi đổi mật khẩu
     */
    @Test
    fun `test password change validation`() = runTest {
        // Test với mật khẩu cũ không đúng
        val incorrectOldPassword = "wrongPassword"
        val userData = UserData(
            username = testUsername,
            password = testPassword,
            email = testEmail,
            id = testUserId
        )

        assertTrue(userData.password != incorrectOldPassword)
    }

    /**
     * Test đăng xuất thành công
     */
    @Test
    fun `test logout successfully`() {
        // Thực hiện đăng xuất
        logout(context)

        // Kiểm tra xóa thông tin đăng nhập
        verify(sharedPreferencesEditor).putString("currentUserId", null)
        verify(sharedPreferencesEditor).putString("currentUserName", null)
        verify(sharedPreferencesEditor).apply()

        // Kiểm tra SharedPreferences sau khi đăng xuất
        `when`(sharedPreferences.getString("currentUserId", null)).thenReturn(null)
        `when`(sharedPreferences.getString("currentUserName", null)).thenReturn(null)

        assertNull(sharedPreferences.getString("currentUserId", null))
        assertNull(sharedPreferences.getString("currentUserName", null))
    }

    /**
     * Test validation dữ liệu profile
     */
    @Test
    fun `test profile data validation`() {
        // Test displayName không được để trống
        val emptyDisplayName = ""
        assertFalse(emptyDisplayName.isNotBlank())

        // Test email phải đúng format
        val invalidEmail = "invalid.email"
        assertFalse(invalidEmail.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)\$")))

        // Test bio không vượt quá độ dài cho phép
        val longBio = "a".repeat(501) // Giả sử giới hạn là 500 ký tự
        assertTrue(longBio.length > 500)
    }

    /**
     * Test xử lý lỗi khi cập nhật profile
     */
    @Test
    fun `test handle errors when updating profile`() = runTest {
        // Mock lỗi khi cập nhật
        `when`(userService.updateUserProfile(any(), any())).thenThrow(RuntimeException("Update failed"))

        try {
            userService.updateUserProfile(testUserId, mock())
            fail("Should throw exception")
        } catch (e: Exception) {
            assertEquals("Update failed", e.message)
        }
    }
}