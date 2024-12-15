package com.example.enggo

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.example.enggo.ui.login.LoginScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.enggo.ui.theme.EngGoTheme

class LoginUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_login_button_is_enabled_when_username_and_password_are_filled() {
        // Thiết lập giao diện người dùng
        composeTestRule.setContent {
            LoginScreen(onLoginClick = {}, redirectToRegister = {})
        }

        // Điền vào các trường tên người dùng và mật khẩu
        composeTestRule.onNodeWithText("Username")
            .performTextInput("testuser")
        composeTestRule.onNodeWithText("Password")
            .performTextInput("password123")

        // Kiểm tra nút Đăng nhập có được kích hoạt không
        composeTestRule.onNodeWithText("Log In")
            .assertIsEnabled()
    }

    @Test
    fun test_login_button_is_disabled_when_username_and_password_are_empty() {
        // Thiết lập giao diện người dùng
        composeTestRule.setContent {
            LoginScreen(onLoginClick = {}, redirectToRegister = {})
        }

        // Kiểm tra nút Đăng nhập bị vô hiệu hóa khi chưa điền thông tin
        composeTestRule.onNodeWithText("Log In")
            .assertIsNotEnabled()
    }

    @Test
    fun verifyEmptyFieldsInitially() {
        composeTestRule.setContent {
            LoginScreen(onLoginClick = {}, redirectToRegister = {})
        }

        composeTestRule
            .onNodeWithText("Username")
            .assertTextContains("")
        composeTestRule
            .onNodeWithText("Password")
            .assertTextContains("")
    }

    @Test
    fun verifyErrorMessageDisplayWhenLoginFails() {
        composeTestRule.setContent {
            LoginScreen(onLoginClick = {}, redirectToRegister = {})
        }

        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("wronguser")
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("wrongpass")
        composeTestRule
            .onNodeWithText("Log In")
            .performClick()

        composeTestRule
            .onNodeWithText("Username or password is incorrect")
            .assertIsDisplayed()
    }

    // Test 1: Kiểm tra hiển thị logo và text welcome
    @Test
    fun verifyInitialScreenContent() {
        composeTestRule.setContent {
            EngGoTheme {
                LoginScreen(onLoginClick = {}, redirectToRegister = {})
            }
        }

        // Kiểm tra logo app
        composeTestRule
            .onNodeWithContentDescription("App Logo")
            .assertExists()
            .assertIsDisplayed()

        // Kiểm tra text heading
        composeTestRule
            .onNodeWithText("Log in to EngGo")
            .assertExists()
            .assertIsDisplayed()

        // Kiểm tra text welcome
        composeTestRule
            .onNodeWithText("Welcome back! Log in to your account to continue.")
            .assertExists()
            .assertIsDisplayed()
    }

    // Kiểm tra hiển thị leading icons của các input fields
    @Test
    fun verifyInputFieldsLeadingIcons() {
        composeTestRule.setContent {
            EngGoTheme {
                LoginScreen(onLoginClick = {}, redirectToRegister = {})
            }
        }

        // Kiểm tra icon username
        composeTestRule
            .onNodeWithContentDescription("Username")
            .assertExists()
            .assertIsDisplayed()

        // Kiểm tra icon password
        composeTestRule
            .onNodeWithContentDescription("Password")
            .assertExists()
            .assertIsDisplayed()
    }

    // Test nhập ký tự đặc biệt vào username
    @Test
    fun inputSpecialCharactersInUsername() {
        composeTestRule.setContent {
            EngGoTheme {
                LoginScreen(onLoginClick = {}, redirectToRegister = {})
            }
        }

        // Nhập ký tự đặc biệt vào username
        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("@#$%^&*()")

        // Kiểm tra button login vẫn enabled
        composeTestRule
            .onNodeWithText("Log In")
            .assertIsEnabled()
    }

    // Test hiệu ứng click vào các components
    @Test
    fun testClickableComponents() {
        var linkClicked = false
        var loginClicked = false

        composeTestRule.setContent {
            EngGoTheme {
                LoginScreen(
                    onLoginClick = { loginClicked = true },
                    redirectToRegister = { linkClicked = true }
                )
            }
        }

        // Test click vào icon show/hide password
        composeTestRule
            .onNodeWithContentDescription("Show Password")
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Hide Password")
            .assertExists()

        // Test click vào link register
        composeTestRule
            .onNodeWithText("Don't have an account? Register")
            .performClick()

        // Verify các actions được trigger
        assert(linkClicked)

        // Test click login button
        composeTestRule
            .onNodeWithText("Username")
            .performTextInput("testuser")
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("testpass")
        composeTestRule
            .onNodeWithText("Log In")
            .performClick()

        assert(loginClicked)
    }

    @Test
    fun testLongTextFieldInput() {
        composeTestRule.setContent {
            EngGoTheme {
                LoginScreen(onLoginClick = {}, redirectToRegister = {})
            }
        }

        // Tạo chuỗi dài (100 ký tự)
        val longUsername = "a".repeat(100)
        val longPassword = "b".repeat(100)

        // Nhập username dài
        composeTestRule
            .onNodeWithText("Username")
            .performTextInput(longUsername)

        // Nhập password dài
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput(longPassword)

        // Kiểm tra nội dung được nhập đầy đủ
        composeTestRule
            .onNode(hasText(longUsername))
            .assertExists()

        composeTestRule
            .onNode(hasText(longPassword))
            .assertExists()

        // Kiểm tra TextField có hiển thị overflow ellipsis (...)
        composeTestRule
            .onNodeWithText("Username")
            .assertTextContains("...")
    }
}
