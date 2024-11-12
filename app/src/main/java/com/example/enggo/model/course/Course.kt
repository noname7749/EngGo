package com.example.enggo.model.course

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Course (
    @SerialName(value = "course_id") val courseId: Int,
    @SerialName(value = "course_name") val courseName: String,
    @SerialName(value = "course_description") val description: String,
    @SerialName(value = "course_level") val level: Int
)

// level
// 1 - elementary
// 2 - pre-intermediate
// 3 - intermediate
// 4 - intermediate plus
// 5 - upper-intermediate
// 6 - advanced
// 7 - ielts