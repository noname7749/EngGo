package com.example.enggo.model.course

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity(tableName = "course")
data class Course (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    val courseId: Int = 0,
    @ColumnInfo(name = "course_name")
    val courseName: String,
    @ColumnInfo(name = "course_description")
    val description: String?,
    @ColumnInfo(name = "course_level")
    val level: Int,
)

// level
// 1 - elementary
// 2 - pre-intermediate
// 3 - intermediate
// 4 - intermediate plus
// 5 - upper-intermediate
// 6 - advanced
// 7 - ielts