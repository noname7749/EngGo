package com.example.enggo.ui.course

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun UnitListRoute(
    courseId: Int?,
    onBackPress: () -> Unit,
) {
    Text(
        text = "YOU JUST CLICK COURSE WITH ID $courseId"
    )
    // TODO()
    //val courseViewModel: CourseViewModel = viewModel(factory = CourseViewModel.Factory)
    UnitListScreen()
}

@Composable
fun UnitListScreen() {
//    Text(
//        text = "Unit List Screen"
//    )
}