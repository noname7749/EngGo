package com.example.enggo.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.home.GenerateQuestionRoute
import com.example.enggo.ui.home.HomeRoute

const val HOME_ROUTE = "home"

const val GPT_ROUTE = "gpt"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavController.navigateToGPTQuestions(navOptions: NavOptions? = null) {
    navigate(GPT_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onRecentCourseClick: (Int, String) -> Unit,
    onGenerateQuestionClick: () -> Unit
) {
    composable(HOME_ROUTE) {
        HomeRoute(
            onRecentCourseClick = onRecentCourseClick,
            onGenerateQuestionClick = onGenerateQuestionClick
        )
    }
}

fun NavGraphBuilder.gptScreen(
    onBackPressed: () -> Unit
) {
    composable(GPT_ROUTE) {
        GenerateQuestionRoute(
            onBackClick = onBackPressed
        )
    }
}