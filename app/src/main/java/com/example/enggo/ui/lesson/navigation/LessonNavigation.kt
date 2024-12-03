package com.example.enggo.ui.lesson.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.lesson.ExerciseRoute
import com.example.enggo.ui.lesson.ExerciseScreen
import com.example.enggo.ui.lesson.LessonRoute
import com.example.enggo.ui.unit.UnitListRoute
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val LESSON_ROUTE = "lesson/{lessonName}/{lessonId}"
const val EXERCISE_ROUTE = "exercise/{lessonName}/{lessonId}/{exerciseIndex}"

fun NavController.navigateToLesson(
    lessonId: Int,
    lessonName: String,
    navOptions: NavOptions? = null
) {
    val encodedLessonName = URLEncoder.encode(lessonName, StandardCharsets.UTF_8.toString())
    Log.d("Navigation", "Navigating to lesson/$encodedLessonName/$lessonId")
    val route = "lesson/$encodedLessonName/$lessonId"
    navigate(route, navOptions)
}

fun NavController.navigateToExerciseScreen(
    lessonId: Int,
    lessonName: String,
    exerciseIndex: Int,
    navOptions: NavOptions? = null
) {
    val encodedLessonName = URLEncoder.encode(lessonName, StandardCharsets.UTF_8.toString())
    val route = "exercise/$encodedLessonName/$lessonId/$exerciseIndex"
    navigate(route, navOptions)
}

fun NavGraphBuilder.lessonScreen(
    onBackPressed: () -> Unit,
    onGoToExercise: (Int, String) -> Unit
) {
    composable(LESSON_ROUTE) { backStackEntry ->
        val lessonId = backStackEntry.arguments?.getString("lessonId")?.toIntOrNull()
        //val lessonName = backStackEntry.arguments?.getString("lessonName")
        val lessonName = backStackEntry.arguments?.getString("lessonName")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        if (lessonId != null && lessonName != null) {
            LessonRoute(
                lessonName = lessonName,
                lessonId = lessonId,
                onBackPress = onBackPressed,
                onGoToExercise = onGoToExercise
            )
        } else {
            Log.e("Navigation", "lessonId or lessonName is null or invalid")
        }
    }
}

fun NavGraphBuilder.exerciseScreens(
    onBackPressed: () -> Unit,
    onNextExercisePressed: (Int, String, Int) -> Unit,
    navController: NavController
) {
    composable(EXERCISE_ROUTE) { backStackEntry ->
        val lessonId = backStackEntry.arguments?.getString("lessonId")?.toIntOrNull()
        val exerciseIndex = backStackEntry.arguments?.getString("exerciseIndex")?.toIntOrNull()
        val lessonName = backStackEntry.arguments?.getString("lessonName")?.let {
            URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
        }
        if (lessonId != null && exerciseIndex != null && lessonName != null) {
            ExerciseRoute(
                lessonName = lessonName,
                lessonId = lessonId,
                exerciseIndex = exerciseIndex,
                onBackPress = onBackPressed,
                onNextLessonPress = onNextExercisePressed,
                onUnitPress = {},
                navController = navController
            )
        } else {
            Log.e("Navigation", "lessonId or exerciseIndex orr lessonName is null or invalid")
        }
    }
}