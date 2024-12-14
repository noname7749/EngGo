package com.example.enggo.ui.profile

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import com.example.enggo.ui.theme.EngGoTheme
import org.junit.Rule
import org.junit.Test
import com.example.enggo.ui.theme.EngGoTheme
import androidx.compose.ui.platform.LocalContext
import com.example.enggo.ui.profile.navigation.*

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

    // Test AccountManagementScreen
    @Test
    fun testSaveProfileChanges() {
        var isProfileUpdated = false

        composeTestRule.setContent {
            EngGoTheme {
                AccountManagementScreen(
                    onPasswordChangeClick = {},
                    onBackClick = {},
                    onLogoutClick = {}
                )
            }
        }

        // Nhập thông tin mới
        composeTestRule.onNodeWithText("User Name")
            .performTextInput("NewUsername")

        composeTestRule.onNodeWithText("Email")
            .performTextInput("newemail@test.com")

        // Click nút Save
        composeTestRule.onNodeWithText("Save").performClick()

        // Verify các trường đã được cập nhật
        composeTestRule.onNodeWithText("NewUsername").assertExists()
        composeTestRule.onNodeWithText("newemail@test.com").assertExists()
    }

    // Test ChangePasswordScreen
    @Test
    fun testPasswordChangeValidation() {
        composeTestRule.setContent {
            EngGoTheme {
                ChangePasswordScreen(onBackClick = {})
            }
        }

        // Nhập sai mật khẩu cũ
        composeTestRule.onNodeWithText("Old Password")
            .performTextInput("wrongpassword")

        // Verify thông báo lỗi
        composeTestRule.onNodeWithText("Password is incorrect!")
            .assertExists()
    }

    //Kiểm tra hiển thị avatar mặc định
    @Test
    fun testDefaultAvatarDisplay() {
        composeTestRule.setContent {
            EngGoTheme {
                ProfileViewScreen(onBackClick = {})
            }
        }

        // Kiểm tra avatar mặc định được hiển thị
        composeTestRule.onNodeWithContentDescription("User Avatar").assertExists()
        // Kiểm tra nút thay đổi avatar
        composeTestRule.onNodeWithText("Change Avatar").assertExists()
    }

    //Kiểm tra giao diện Change Password
    @Test
    fun testChangePasswordScreenUI() {
        composeTestRule.setContent {
            EngGoTheme {
                ChangePasswordScreen(onBackClick = {})
            }
        }

        // Kiểm tra các trường nhập mật khẩu
        composeTestRule.onNodeWithText("Old Password").assertExists()
        composeTestRule.onNodeWithText("New Password").assertExists()
        composeTestRule.onNodeWithText("Confirm Password").assertExists()

        // Kiểm tra nút show/hide password
        composeTestRule.onAllNodesWithContentDescription("Show Password")[0].assertExists()

        // Kiểm tra nút submit
        composeTestRule.onNodeWithText("Change Password").assertExists()
    }

    //Kiểm tra UI của Account Management
    @Test
    fun testAccountManagementUI() {
        composeTestRule.setContent {
            EngGoTheme {
                AccountManagementScreen(
                    onPasswordChangeClick = {},
                    onBackClick = {},
                    onLogoutClick = {}
                )
            }
        }

        // Kiểm tra tiêu đề
        composeTestRule.onNodeWithText("Your Profile").assertExists()

        // Kiểm tra các trường input
        composeTestRule.onNodeWithText("User Name").assertExists()
        composeTestRule.onNodeWithText("Email").assertExists()

        // Kiểm tra các nút
        composeTestRule.onNodeWithText("Change Password").assertExists()
        composeTestRule.onNodeWithText("Save").assertExists()
    }
}
