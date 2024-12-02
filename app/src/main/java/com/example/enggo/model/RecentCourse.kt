package com.example.enggo.model

import com.example.enggo.model.course.Course

data class RecentCourse(
    val course: Course,
    val last_accessed: Long
)
