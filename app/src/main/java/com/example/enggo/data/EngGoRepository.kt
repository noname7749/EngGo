package com.example.enggo.data

import com.example.enggo.model.course.Course
import com.example.enggo.network.EngGoApiService
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Repository that fetch courses list from EngGoApi.
 */
interface EngGoRepository {
    /** Fetches list of MarsPhoto from marsApi */
    suspend fun getCourses(): List<Course>
}

class NetworkEngGoRepository(
    private val engGoApiService: EngGoApiService
) : EngGoRepository {
    override suspend fun getCourses(): List<Course> = engGoApiService.getCourses()

}