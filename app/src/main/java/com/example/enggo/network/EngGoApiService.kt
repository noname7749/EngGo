package com.example.enggo.network

import com.example.enggo.model.course.Course
import retrofit2.http.GET

interface EngGoApiService {
    /**
     * Returns a [List] of [Course] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("course")
    suspend fun getCourses(): List<Course>
}