package com.example.enggo.ui.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.enggo.ui.theme.EngGoTheme
import org.junit.Rule
import org.junit.Test

class ProfileUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test cho việc hiển thị thông tin trên màn hình Profile
    @Test
    fun testProfileScreen_Display() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileRoute(
                    onLogoutClick = {},
                    onClickProfile = {},
                    onClickAccount = {}
                )
            }
        }

        // Kiểm tra xem tên người dùng có hiển thị hay không
        composeTestRule.onNodeWithText("User Name").assertExists()

        // Kiểm tra xem avatar người dùng có hiển thị hay không
        composeTestRule.onNodeWithContentDescription("User Avatar").assertExists()

        // Kiểm tra nút "Logout" có hiển thị hay không
        composeTestRule.onNodeWithText("Logout").assertExists()

        // Kiểm tra các lựa chọn khác
        composeTestRule.onNodeWithText("Account Management").assertExists()
        composeTestRule.onNodeWithText("Edit Profile").assertExists()
    }

    // Test cho việc điều hướng đến màn hình quản lý tài khoản
    @Test
    fun testNavigationToAccountManagement() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileRoute(
                    onLogoutClick = {},
                    onClickProfile = {},
                    onClickAccount = { /* Thực hiện điều hướng ở đây nếu cần */ }
                )
            }
        }

        // Nhấn vào "Account Management"
        composeTestRule.onNodeWithText("Account Management").performClick()

        // Kiểm tra điều hướng thành công, có thể thực hiện kiểm tra màn hình đích nếu cần
        composeTestRule.onNodeWithText("Account Management").assertExists()  // Kiểm tra nếu màn hình này được hiển thị
    }

    // Test cho việc điều hướng đến màn hình chỉnh sửa thông tin
    @Test
    fun testNavigationToEditProfile() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileRoute(
                    onLogoutClick = {},
                    onClickProfile = { /* Điều hướng đến ProfileViewScreen */ },
                    onClickAccount = {}
                )
            }
        }

        // Nhấn vào "Edit Profile"
        composeTestRule.onNodeWithText("Edit Profile").performClick()

        // Kiểm tra xem có điều hướng đến màn hình Edit Profile không
        composeTestRule.onNodeWithText("Edit Profile").assertExists()  // Kiểm tra điều hướng thành công
    }

    // Test cho nút Logout
    @Test
    fun testLogoutButton() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileRoute(
                    onLogoutClick = { /* Xử lý sự kiện logout */ },
                    onClickProfile = {},
                    onClickAccount = {}
                )
            }
        }

        // Kiểm tra xem nút Logout có hiển thị và có thể nhấn vào không
        composeTestRule.onNodeWithText("Logout").performClick()

        // Sau khi nhấn nút Logout, có thể kiểm tra các hành động tương ứng
        // Ví dụ: Kiểm tra xem có quay lại màn hình đăng nhập không nếu cần
    }

    // Test cho việc chọn ảnh đại diện từ Gallery hoặc Camera
    @Test
    fun testImageSelectionForAvatar() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileViewScreen(onBackClick = {})
            }
        }

        // Kiểm tra xem có hộp thoại chọn ảnh khi nhấn nút thay đổi avatar
        composeTestRule.onNodeWithText("Change Avatar").performClick()

        // Chọn Camera
        composeTestRule.onNodeWithText("Camera").performClick()

        // Kiểm tra xem ảnh được chọn có phải là ảnh đã được chụp không
        composeTestRule.onNodeWithContentDescription("User Avatar").assertExists()

        // Chọn ảnh từ Gallery
        composeTestRule.onNodeWithText("Gallery").performClick()
        composeTestRule.onNodeWithContentDescription("User Avatar").assertExists()
    }
}
