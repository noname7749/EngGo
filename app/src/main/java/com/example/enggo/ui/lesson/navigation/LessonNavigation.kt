package com.example.enggo.ui.lesson.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.lesson.LessonRoute
import com.example.enggo.ui.unit.UnitListRoute

const val LESSON_ROUTE = "lesson/{lessonId}"

fun NavController.navigateToLesson(
    lessonId: Int,
    navOptions: NavOptions? = null
) {
    Log.d("Navigation", "Navigating to lesson/$lessonId")
    val route = "lesson/$lessonId"
    navigate(route, navOptions)
}

fun NavGraphBuilder.lessonScreen(
    onBackPressed: () -> Unit,
) {
    composable(LESSON_ROUTE) { backStackEntry ->
        val lessonId = backStackEntry.arguments?.getString("lessonId")?.toIntOrNull()
        if (lessonId != null) {
            LessonRoute(
                lessonId = lessonId,
                onBackPress = onBackPressed
            )
        } else {
            Log.e("Navigation", "lessonId is null or invalid")
        }
    }
}