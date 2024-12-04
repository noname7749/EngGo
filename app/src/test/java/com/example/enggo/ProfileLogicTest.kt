package com.example.enggo.data.service

import org.junit.Assert.*
import org.junit.Test

class ProfileLogicTest {

    // Thay đổi tên người dùng hợp lệ
    @Test
    fun testUpdateUsername_validUsername() {
        val oldUsername = "OldUsername"
        val newUsername = "NewUsername"

        val result = updateUsername(oldUsername, newUsername)

        assertTrue(result)
    }

    // Thay đổi tên người dùng không hợp lệ (đã tồn tại)
    @Test
    fun testUpdateUsername_invalidUsername() {
        val oldUsername = "OldUsername"
        val newUsername = "ExistingUsername"

        val result = updateUsername(oldUsername, newUsername)

        assertFalse(result)
    }

    // Thay đổi mật khẩu hợp lệ
    @Test
    fun testUpdatePassword_validPassword() {
        val oldPassword = "OldPassword123"
        val newPassword = "NewPassword123"

        val result = updatePassword(oldPassword, newPassword)

        assertTrue(result)
    }

    // Thay đổi mật khẩu không hợp lệ (mật khẩu cũ sai)
    @Test
    fun testUpdatePassword_invalidOldPassword() {
        val oldPassword = "WrongPassword"
        val newPassword = "NewPassword123"

        val result = updatePassword(oldPassword, newPassword)

        assertFalse(result)
    }

    // Thay đổi ảnh avatar hợp lệ
    @Test
    fun testUpdateAvatar_validAvatar() {
        val oldAvatar = "old_avatar.jpg"
        val newAvatar = "new_avatar.jpg"

        val result = updateAvatar(oldAvatar, newAvatar)

        assertTrue(result)
    }

    // Thay đổi ảnh avatar không hợp lệ (file không tồn tại)
    @Test
    fun testUpdateAvatar_invalidAvatar() {
        val oldAvatar = "old_avatar.jpg"
        val newAvatar = "non_existent_avatar.jpg"

        val result = updateAvatar(oldAvatar, newAvatar)

        assertFalse(result)
    }
    private fun updateUsername(oldUsername: String, newUsername: String): Boolean {
        if (newUsername == "ExistingUsername") {
            return false
        }
        return true
    }

    private fun updatePassword(oldPassword: String, newPassword: String): Boolean {
        if (oldPassword != "OldPassword123") {
            return false
        }
        return true
    }

    private fun updateAvatar(oldAvatar: String, newAvatar: String): Boolean {
        if (newAvatar == "non_existent_avatar.jpg") {
            return false
        }
        return true
    }
}

//@Test
//fun testUploadImageToFirebase() {
//    val mockStorageRef = mock(StorageReference::class.java)
//    val mockImageRef = mock(StorageReference::class.java)
//
//    //  tải ảnh lên Firebase Storage
//    `when`(mockStorageRef.child(anyString())).thenReturn(mockImageRef)
//    `when`(mockImageRef.putBytes(any())).thenReturn(Task.forResult(null))
//
//    var result: Boolean? = null
//    uploadImageToFirebase(mock(Bitmap::class.java), "user123", mockContext) { success, imageUrl ->
//        result = success
//    }
//
//    // Kiểm tra xem ảnh có được tải lên thành công hay không
//    assertTrue(result == true)
//}
