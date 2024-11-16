package com.example.enggo.data.repository

import com.example.enggo.model.course.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {
    fun getAllCourses(): Flow<List<Course>>
}