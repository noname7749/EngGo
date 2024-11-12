package com.example.enggo.model.course

data class UnitData (
    val unitId: Int,
    val unitName: String,
    val lesson: List<Lesson>
)