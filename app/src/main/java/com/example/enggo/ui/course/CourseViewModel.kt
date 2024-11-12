package com.example.enggo.ui.course

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.enggo.EngGoApplication
import com.example.enggo.data.EngGoRepository
import com.example.enggo.model.course.Course
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CourseUiState {
    data class Success(val courses: List<Course>) : CourseUiState
    object Error : CourseUiState
    object Loading : CourseUiState
}

class CourseViewModel(private val engGoRepository: EngGoRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var courseUiState: CourseUiState by mutableStateOf(CourseUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     */
    init {
        getCourses()
    }

    /**
     * Gets Course information from the EngGo API Retrofit service and updates the
     * [Course] [List] [MutableList].
     */
    fun getCourses() {
        viewModelScope.launch {
            courseUiState = CourseUiState.Loading
            courseUiState = try {
                CourseUiState.Success(engGoRepository.getCourses())
            } catch (e: IOException) {
                Log.e("getCourses", "Error fetching courses", e)
                CourseUiState.Error
            } catch (e: HttpException) {
                Log.e("getCourses", "Error fetching courses", e)
                CourseUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as EngGoApplication)
                val engGoRepository = application.container.engGoRepository
                CourseViewModel(engGoRepository = engGoRepository)
            }
        }
    }
}
