package com.example.enggo.data.repository

import com.example.enggo.model.ProfileData
import com.example.enggo.model.UserData
import com.example.enggo.model.course.Course
import com.example.enggo.model.dictionary.WordModel


interface UserRepository {
    suspend fun addUserData(userData: UserData): String?
    suspend fun getUserDataById(userId: String): UserData?
    suspend fun getUserDataByUsername(userName : String): String?
    suspend fun updateUserData(userId: String, userData: UserData)
    suspend fun deleteUserDataById(userId: String)
    suspend fun checkUsernameAvailability(userName: String): Boolean
    suspend fun updateUserProfile(userId: String, profileData: ProfileData)
    suspend fun checkEmailAvailability(email: String): Boolean
    suspend fun verifyLoginInfo(userName: String, password: String): Boolean
    suspend fun getUserIdByUsername(userName: String): String?
    suspend fun getUserProfile(userId: String): ProfileData?
    suspend fun getUserAvatarFromUserId(userId: String): String?
    suspend fun getUserAvatarFromUsername(username: String): String?
    fun addFCMToken(token: String, userId: String)
    suspend fun getFCMToken(userId: String): String
    suspend fun getUsernameById(userId: String): String?

    //them bookmarks nhu subcollection cua user
    suspend fun addBookmark(userId: String, word: WordModel): Boolean
    suspend fun getBookmarks(userId: String): List<WordModel>
    suspend fun removeBookmark(userId: String, wordsetId: String)

    suspend fun addRecentCourses(userId: String, course: Course) : Boolean
    suspend fun isCourseExistInRecent(userId: String, courseId: Int): Boolean
    suspend fun updateCourseTimestamp(userId: String, courseId: Int): Boolean
    suspend fun getRecentCourses(userId: String): List<Map<String, Any>>

    suspend fun updateLoginStreak(userId: String): Boolean
    suspend fun getUserLoginStreak(userId: String): Int
}