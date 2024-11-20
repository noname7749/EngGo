package com.example.enggo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.enggo.ui.course.navigation.coursesScreen
import com.example.enggo.ui.unit.navigation.navigateToUnitList
import com.example.enggo.ui.unit.navigation.unitListScreen
import com.example.enggo.ui.home.navigation.HOME_ROUTE
import com.example.enggo.ui.home.navigation.homeScreen
import com.example.enggo.ui.home.navigation.navigateToHome
import com.example.enggo.ui.lesson.navigation.lessonScreen
import com.example.enggo.ui.lesson.navigation.navigateToLesson
import com.example.enggo.ui.login.navigation.LOGIN_ROUTE
import com.example.enggo.ui.login.navigation.loginScreen
import com.example.enggo.ui.login.navigation.navigateToLogin
import com.example.enggo.ui.navigation.AppState
import com.example.enggo.ui.register.navigation.navigateToRegister
import com.example.enggo.ui.register.navigation.registerScreen

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: String = if (appState.isTimeoutSession) LOGIN_ROUTE else HOME_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        homeScreen()
        coursesScreen(onCourseClick = navController::navigateToUnitList)
        unitListScreen(onBackPressed = navController::popBackStack, onLessonPressed = navController::navigateToLesson)
        lessonScreen (onBackPressed = navController::popBackStack)
        registerScreen (onRegisterClick = navController::navigateToLogin, redirectToLogin = navController::navigateToLogin)
        loginScreen (onLoginClick = navController::navigateToHome , redirectToRegister = navController::navigateToRegister)
    }
}