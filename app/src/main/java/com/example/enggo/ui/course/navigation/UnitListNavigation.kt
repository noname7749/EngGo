package com.example.enggo.ui.course.navigation

import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.enggo.ui.course.UnitListRoute

const val UNIT_LIST_ROUTE = "course/{courseId}"

fun NavController.navigateToUnitList(
    courseId: Int,
    navOptions: NavOptions? = null
) {
    Log.d("Navigation", "Navigating to course/$courseId")
    val route = "course/$courseId"
    navigate(route, navOptions)
}

fun NavGraphBuilder.unitListScreen(
    onBackPressed: () -> Unit,
) {
    composable(UNIT_LIST_ROUTE) { backStackEntry ->
//        val id = backStackEntry.arguments?.getInt("courseId")
//        UnitListRoute(
//            courseId = id,
//            onBackPress = onBackPressed,
//        )
        val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull() // Chuyển đổi String thành Int
        if (courseId != null) {
            UnitListRoute(
                courseId = courseId,
                onBackPress = onBackPressed
            )
        } else {
            Log.e("Navigation", "courseId is null or invalid") // Log nếu courseId không hợp lệ
        }
    }
}