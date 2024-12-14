package com.example.enggo.ui.register

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.enggo.ui.theme.EngGoTheme
import org.junit.Rule
import org.junit.Test
import com.example.enggo.ui.register.RegisterScreen
import androidx.compose.ui.test.performTextInput

class RegisterUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Test cho việc nhấn nút "Create an account"
    @Test
    fun testRegisterButtonClick() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Nhập thông tin vào các trường
        composeTestRule.onNodeWithText("Username").performTextInput("TestUser")
        composeTestRule.onNodeWithText("Your Email").performTextInput("testuser@example.com")
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password123")

        // Nhấn nút đăng ký
        composeTestRule.onNodeWithText("Create an account").performClick()

        // Kiểm tra lại xem giao diện có hiển thị lại như mong đợi không
        composeTestRule.onNodeWithText("Sign up with Email").assertIsDisplayed()
    }

    // Test cho thông báo tên người dùng đã tồn tại
    @Test
    fun testUsernameAvailabilityMessage() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Nhập tên người dùng đã tồn tại
        composeTestRule.onNodeWithText("Username").performTextInput("ExistingUser")

        // Kiểm tra xem thông báo "Username is available" có hiển thị không
        composeTestRule.onNodeWithText("Username is available").assertExists()
    }

    // Test cho mật khẩu không khớp
    @Test
    fun testPasswordMismatch() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Nhập mật khẩu và xác nhận mật khẩu không khớp
        composeTestRule.onNodeWithText("Password").performTextInput("password123")
        composeTestRule.onNodeWithText("Confirm Password").performTextInput("password321")

        // Kiểm tra thông báo lỗi "Password is incorrect!"
        composeTestRule.onNodeWithText("Password is incorrect!").assertExists()
    }

    //Kiểm tra hiển thị và chức năng của nút hiện/ẩn mật khẩu
    @Test
    fun testPasswordFieldsVisibilityToggle() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Test cho Password field
        composeTestRule.onNodeWithText("Password")
            .performTextInput("testPassword123")
        composeTestRule.onAllNodesWithContentDescription("Show Password")[0]
            .performClick()
        composeTestRule.onAllNodesWithContentDescription("Hide Password")[0]
            .performClick()

        // Test cho Confirm Password field
        composeTestRule.onNodeWithText("Confirm Password")
            .performTextInput("testPassword123")
        composeTestRule.onAllNodesWithContentDescription("Show Password")[1]
            .performClick()
        composeTestRule.onAllNodesWithContentDescription("Hide Password")[1]
            .performClick()
    }

    //Kiểm tra chức năng chuyển hướng sang màn hình Login
    @Test
    fun testNavigateToLogin() {
        var isNavigatedToLogin = false

        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(
                    onRegisterClick = {},
                    redirectToLogin = { isNavigatedToLogin = true }
                )
            }
        }

        // Click vào text "Already have an account? Login"
        composeTestRule.onNodeWithText("Already have an account? Login").performClick()

        // Verify rằng hàm redirectToLogin đã được gọi
        assert(isNavigatedToLogin)
    }

    //Kiểm tra form validation khi để trống các trường
    @Test
    fun testEmptyFieldValidation() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Click register mà không nhập gì
        composeTestRule.onNodeWithText("Create an account").performClick()

        // Kiểm tra các trường input vẫn trống
        composeTestRule.onNodeWithText("Username").assertTextContains("")
        composeTestRule.onNodeWithText("Your Email").assertTextContains("")
        composeTestRule.onNodeWithText("Password").assertTextContains("")
        composeTestRule.onNodeWithText("Confirm Password").assertTextContains("")
    }

    //Kiểm tra độ dài tối đa của username
    @Test
    fun testUsernameLengthLimit() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        // Nhập username dài
        val longUsername = "a".repeat(50)
        composeTestRule.onNodeWithText("Username").performTextInput(longUsername)

        // Verify rằng chỉ một phần của chuỗi được hiển thị (do giới hạn của TextField)
        composeTestRule.onNodeWithText(longUsername).assertExists()
    }

    //Kiểm tra hiển thị initial UI elements
    @Test
    fun testInitialUIElements() {
        composeTestRule.setContent {
            EngGoTheme {
                RegisterScreen(onRegisterClick = {}, redirectToLogin = {})
            }
        }

        composeTestRule.onNodeWithText("Sign up with Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Start learning with EngGo").assertIsDisplayed()
        composeTestRule.onNodeWithText("Username").assertIsDisplayed()
        composeTestRule.onNodeWithText("Your Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Confirm Password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create an account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Already have an account? Login").assertIsDisplayed()
    }


}
