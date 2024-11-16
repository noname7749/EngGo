package com.example.enggo.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.enggo.model.course.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseSearchDao {
    @Query("SELECT * FROM course;")
    fun getCourses(): Flow<List<Course>>
}