package com.example.enggo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.enggo.data.service.CourseService
import com.example.enggo.data.service.UserService
import com.example.enggo.model.RecentCourse
import com.example.enggo.model.course.Course
import com.example.enggo.ui.course.CourseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val courseService: CourseService,
    private val userService: UserService,
    userId: String
) : ViewModel() {

    private val _recentCourses = MutableStateFlow<List<RecentCourse>>(emptyList())
    val recentCourses: StateFlow<List<RecentCourse>> = _recentCourses

    init {
        fetchRecentCourses(userId = userId)
    }

    fun fetchRecentCourses(userId: String) {
        viewModelScope.launch {
            try {
                val recentCoursesData = userService.getRecentCourses(userId)


                val recentCoursesList = mutableListOf<RecentCourse>()
                for (recentCourse in recentCoursesData) {
                    val courseId = recentCourse["course_id"] as? Long
                    val timestamp = recentCourse["timestamp"] as? com.google.firebase.Timestamp
                    val lastAccessed = timestamp?.seconds?.times(1000)

                    if (courseId != null && lastAccessed != null) {
                        val course = courseService.getCourseById(courseId.toInt())

                        course?.let {
                            recentCoursesList.add(RecentCourse(course = it, last_accessed = lastAccessed))
                        }
                    }
                }

                val sortedCourses = recentCoursesList.sortedByDescending { it.last_accessed }

                _recentCourses.value = sortedCourses.take(5)

            } catch (e: Exception) {
                _recentCourses.value = emptyList()
            }
        }
    }

    fun checkAndUpdateRecentCourses(userId: String, course: Course) {
        viewModelScope.launch {
            val isExist = userService.isCourseExistInRecent(userId, course.course_id)
            if (isExist) {
                userService.updateCourseTimestamp(userId, course.course_id)
            } else {
                userService.addRecentCourses(userId, course)
            }
        }
    }
}

class HomeViewModelFactory(private val courseService: CourseService, private val userService: UserService, private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(courseService, userService, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}