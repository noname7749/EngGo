package com.example.enggo.data.service

import android.util.Log
import com.example.enggo.data.repository.CourseRepository
import com.example.enggo.model.course.Course
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class CourseService(private val firestore: FirebaseFirestore) : CourseRepository {
    override suspend fun getCourseById(courseId: Int): Course? {
        return try {
            val documentSnapshot = firestore.collection("course")
                .document(courseId.toString())
                .get()
                .await()

            if (documentSnapshot.exists()) {
                documentSnapshot.toObject(Course::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error fetching course $courseId: ", e)
            null
        }
    }

    override fun getAllCourses(): CollectionReference {
        return firestore.collection("course")
    }

}