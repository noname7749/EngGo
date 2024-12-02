package com.example.enggo.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.home.HomeRoute

const val HOME_ROUTE = "home"

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    navigate(HOME_ROUTE, navOptions)
}

fun NavGraphBuilder.homeScreen(
    onRecentCourseClick: (Int, String) -> Unit,
) {
    composable(HOME_ROUTE) {
        HomeRoute(onRecentCourseClick = onRecentCourseClick)
    }
}