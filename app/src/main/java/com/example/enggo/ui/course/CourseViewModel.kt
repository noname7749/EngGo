package com.example.enggo.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.enggo.data.repository.CourseRepository
import com.example.enggo.data.service.CourseService
import com.example.enggo.data.service.UserService
import com.example.enggo.model.course.Course
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CourseUiState(
    val courseList: List<Course> = listOf()
)

class CourseViewModel(
    private val courseService: CourseService,
    private val userService: UserService
) : ViewModel() {
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    private var courseListener: ListenerRegistration? = null

    init {
        fetchCourses()
    }

    private fun fetchCourses() {
        courseListener?.remove()
        courseListener = courseService.getAllCourses().addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                // Error Handler
                return@addSnapshotListener
            }

            querySnapshot?.let {it ->
                val fetchedCourses = it.documents.mapNotNull { document ->
                    document.toObject(Course::class.java)
                }
                _courses.value = fetchedCourses

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

    override fun onCleared() {
        super.onCleared()
        courseListener?.remove()
    }
}

class CourseViewModelFactory(private val courseService: CourseService, private val userService: UserService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(courseService, userService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
