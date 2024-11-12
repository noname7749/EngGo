package com.example.enggo.model.course

data class Exercise(
    val exerciseId: Int,
    val exerciseTitle: String,
    val score: Int,
    val questions: List<Question> // Danh sách câu hỏi
)