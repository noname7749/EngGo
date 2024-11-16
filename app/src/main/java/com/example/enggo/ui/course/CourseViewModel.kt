package com.example.enggo.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.enggo.data.repository.CourseRepository
import com.example.enggo.model.course.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class CourseUiState(
    val courseList: List<Course> = listOf()
)

class CourseViewModel(
    val courseRepository: CourseRepository
) : ViewModel() {

    val courseUiState: StateFlow<CourseUiState> =
        courseRepository.getAllCourses().map { CourseUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CourseUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L

    }
}
