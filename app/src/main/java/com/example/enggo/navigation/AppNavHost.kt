package com.example.enggo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.enggo.ui.course.navigation.coursesScreen
import com.example.enggo.ui.home.navigation.HOME_ROUTE
import com.example.enggo.ui.home.navigation.homeScreen
import com.example.enggo.ui.navigation.AppState

@Composable
fun AppNavHost(
    appState: AppState,
    modifier: Modifier = Modifier,
    startDestination: String = HOME_ROUTE,
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        homeScreen()
        coursesScreen()
    }
}