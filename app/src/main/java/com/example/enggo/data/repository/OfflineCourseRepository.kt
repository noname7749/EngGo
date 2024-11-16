package com.example.enggo.data.repository

import com.example.enggo.data.database.CourseSearchDao
import com.example.enggo.model.course.Course
import kotlinx.coroutines.flow.Flow

class OfflineCourseRepository(
    private val courseSearchDao: CourseSearchDao
) : CourseRepository {
    override fun getAllCourses(): Flow<List<Course>> = courseSearchDao.getCourses()

}