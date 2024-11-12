package com.example.enggo.model.course

data class Lesson(
    val lessonId: Int,
    val lessonName: String,
    val hasTheory: Boolean,
    val hasExercise: Boolean,
    val theory: Theory? = null, // Thông tin lý thuyết (nếu có)
    val exercise: Exercise? = null // Thông tin bài tập (nếu có)
)