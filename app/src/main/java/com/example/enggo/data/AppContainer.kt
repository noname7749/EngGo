package com.example.enggo.data

import android.content.Context
import com.example.enggo.data.database.CourseDatabase
import com.example.enggo.data.repository.CourseRepository
import com.example.enggo.data.repository.OfflineCourseRepository

interface AppContainer {
    val courseRepository: CourseRepository;
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val courseRepository: CourseRepository by lazy {
        OfflineCourseRepository(CourseDatabase.getDatabase(context).courseSearchDao())
    }
}