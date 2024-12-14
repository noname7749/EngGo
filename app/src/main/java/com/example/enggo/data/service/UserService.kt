package com.example.enggo.data.service

import android.util.Log
import com.example.enggo.data.repository.UserRepository
import com.example.enggo.model.ProfileData
import com.example.enggo.model.UserData
import com.example.enggo.model.course.Course
import com.example.enggo.model.dictionary.WordModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class UserService(private val firestore: FirebaseFirestore): UserRepository {
    override suspend fun addUserData(userData: UserData): String? {
        var result: String? = null
        firestore.collection("users").
        document(userData.id).set(userData)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Created user successfully: $userData")
                result = userData.toString()
            }
            .addOnFailureListener {exception ->
                Log.e("FIRESTORE ERROR", "Error adding user data to Firestore: $exception")
            }.await()
        updateUserProfile(userData.id, ProfileData(userData.username, id = userData.id))
        return result
    }

    override suspend fun getUserDataById(userId: String): UserData? {
        try {
            val document = firestore.collection("users").document(userId).get().await()
            val userName = document.data?.get("username").toString()
            val password = document.data?.get("password").toString()
            val profile = document.data?.get("profile").toString()
            val email = document.data?.get("email").toString()
            val id = document.data?.get("id").toString()
            val phone = document.data?.get("phone").toString()
            return UserData(userName, password, email, phone, id, profile)
        } catch (e: Exception) {
            Log.e("UserService", "Error fetching user data: $e")
        }
        return null//user
    }

    override suspend fun getUserDataByUsername(userName: String): String? {
        var user: String? = null
        firestore.collection("users")
            .whereEqualTo("username", userName).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null) {
                    user = querySnapshot.documents[0].data.toString()
                    Log.d("FIRESTORE", "Get user with username: $userName successfully")
                    Log.d("FIRESTORE", "User: $user")
                } else {
                    Log.d("FIRESTORE ERROR", "User not found with username: $userName")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE ERROR", "Error getting user: ", exception)
            }.await()
        return user
    }

    override suspend fun getUserIdByUsername(userName: String): String? {
        var id: String? = null
        firestore.collection("users")
            .whereEqualTo("username", userName).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot != null && querySnapshot.documents.size > 0) {
                    id = querySnapshot.documents[0].data?.get("id")?.toString()
                    Log.d("FIRESTORE", "Get user with username: $userName successfully")
                    Log.d("FIRESTORE", "User ID: $id")
                } else {
                    Log.d("FIRESTORE ERROR", "User not found with username: $userName")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE ERROR", "Error getting user: ", exception)
            }.await()
        return id
    }

    override suspend fun updateUserData(userId: String, userData: UserData) {
        val user = getUserDataById(userId)
        if (user != null) {
            firestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener {
                    Log.d("FIRESTORE", "Updated user successfully: $userData")
                }
                .addOnFailureListener { exception ->
                    Log.e("FIRESTORE ERROR", "Error update user data to Firestore: $exception")
                }.await()
        }
        else {
            Log.d("FIRESTORE ERROR", "User not found with ID: $userId")
        }
    }

    override suspend fun deleteUserDataById(userId: String) {
        val user = getUserDataById(userId)
        if (user != null) {
            firestore.collection("users").document(userId).delete()
                .addOnSuccessListener {
                    Log.d("FIRESTORE", "Deleted user with ID: $userId successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("FIRESTORE ERROR", "Error deleting user data: $exception")
                }.await()
        }
        else {
            Log.d("FIRESTORE ERROR", "User not found with ID: $userId")
        }
    }

    override suspend fun checkUsernameAvailability(userName: String): Boolean {
        val usernamesRef = firestore.collection("users")
            .whereEqualTo("username", userName)
        val querySnapshot = usernamesRef.get().await()
        if (!querySnapshot.isEmpty) {
            Log.e("FIRESTORE ERROR", "Username '$userName' already exists. Please choose a different one.")
            return true
        }
        return false
    }

    override suspend fun checkEmailAvailability(email: String): Boolean {
        val usernamesRef = firestore.collection("users")
            .whereEqualTo("email", email)
        val querySnapshot = usernamesRef.get().await()
        if (!querySnapshot.isEmpty) {
            Log.e("FIRESTORE ERROR", "Email '$email' already exists. Please choose a different one.")
            return true
        }
        return false
    }

    override suspend fun verifyLoginInfo(userName: String, password: String): Boolean {
        val usernamesRef = firestore.collection("users")
            .whereEqualTo("username", userName).whereEqualTo("password", password)
        val querySnapshot = usernamesRef.get().await()
        if (!querySnapshot.isEmpty) {
            Log.d("FIRESTORE", "Login successfully")
            return true
        }
        Log.e("FIRESTORE", "Login info incorrect")
        return false
    }

    override suspend fun updateUserProfile(userId: String, profileData: ProfileData) {
        firestore.collection("profiles").document(userId).set(profileData)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Updated user's profile successfully: $profileData")
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE ERROR", "Error update user's profile data to Firestore: $exception")
            }.await()
    }

    suspend fun updateProfileImageUrl(userId: String, imageUrl: String) {
        try {
            firestore.collection("profiles").document(userId).update("avatar", imageUrl).await()
            Log.d("FIRESTORE", "Updated user's profile image URL successfully: $imageUrl")
        } catch (exception: Exception) {
            Log.e("FIRESTORE ERROR", "Error updating user's profile image URL in Firestore: $exception")
        }
    }

    override suspend fun getUserProfile(userId: String): ProfileData? {
        try {
            val document = firestore.collection("profiles").document(userId).get().await()
            val displayName = document.data?.get("displayName").toString()
            val dob = document.data?.get("dob").toString()
            val avatar = document.data?.get("avatar").toString()
            val bio = document.data?.get("bio").toString()
            return ProfileData(displayName, avatar, bio, userId)
        } catch (exception: Exception) {
            Log.e("FIRESTORE ERROR", "Error getting user's profile data to Firestore: $exception")
        }
        return null
    }

    override suspend fun getUserAvatarFromUserId(userId: String): String? {
        try {
            val document = firestore.collection("profiles").document(userId).get().await()
            if (document.exists()) {
                val avatar = document.data?.get("avatar")
                if (avatar != null) {
                    Log.d("FIRESTORE", "Avatar for user $userId: $avatar")
                    return avatar.toString()
                } else {
                    Log.d("FIRESTORE", "No avatar field for user $userId")
                }
            } else {
                Log.d("FIRESTORE", "No document for user $userId")
            }
        } catch (exception: Exception) {
            Log.e("FIRESTORE ERROR", "Error getting user's avatar URL from Firestore: $exception")
        }
        return null
    }

    override suspend fun getUserAvatarFromUsername(username: String): String? {
        try {
            val document = firestore.collection("users").whereEqualTo("username", username).get().await()
            if (!document.isEmpty) {
                val userId = document.documents[0].data?.get("id").toString()
                return getUserAvatarFromUserId(userId)
            }
        } catch (exception: Exception) {
            Log.e("FIRESTORE ERROR", "Error getting user's avatar URL from Firestore: $exception")
        }
        return null
    }

    override fun addFCMToken(token: String, userId: String) {
        firestore.collection("users").document(userId).update("fcmToken", token)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Add user's FCM Token successfully: $token")
            }
            .addOnFailureListener { exception ->
                Log.e("FIRESTORE ERROR", "Error adding user's FCM Token to Firestore: $exception")
            }
    }

    override suspend fun getFCMToken(userId: String): String {
        var token: String? = null
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                token = document.data?.get("fcmToken").toString()
                Log.d("TOKEN", "FCM Token: $token")
            }
        return token.toString()
    }

    override suspend fun getUsernameById(userId: String): String? {
        var username: String? = null
        try {
            val document = firestore.collection("users").document(userId).get().await()
            username = document.data?.get("username").toString()
            Log.d("FIRESTORE", "Get username successfully: $username")
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error getting username: ", e)
        }
        return username
    }

    override suspend fun addBookmark(userId: String, word: WordModel): Boolean {
        return try {
            firestore.collection("users").document(userId)
                .collection("bookmarks").document(word.wordsetId) // wordsetId la key cho document
                .set(word)
                .await()
            Log.d("FIRESTORE", "Adding bookmark successfully: ${word.word}")
            true
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error adding bookmark: ", e)
            false
        }
    }

    override suspend fun getBookmarks(userId: String): List<WordModel> {
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("bookmarks")
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(WordModel::class.java) }
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error fetching bookmarks: ", e)
            emptyList()
        }
    }

    override suspend fun removeBookmark(userId: String, wordsetId: String) {
        firestore.collection("users").document(userId)
            .collection("bookmarks").document(wordsetId).delete().await()
    }

    override suspend fun addRecentCourses(userId: String, course: Course): Boolean {
        val courseMap = mapOf(
            "course_id" to course.course_id,
            "timestamp" to Date()
        )

        return try {
            firestore.collection("users")
                .document(userId)
                .collection("recent_courses")
                .document(course.course_id.toString())
                .set(courseMap)
                .await()
            Log.d("FIRESTORE", "Adding recent course successfully: ${course.course_name}")
            true
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error adding course: ", e)
            false
        }
    }

    override suspend fun isCourseExistInRecent(userId: String, courseId: Int): Boolean {
        return try {
            val documentSnapshot = firestore.collection("users")
                .document(userId)
                .collection("recent_courses")
                .document(courseId.toString())
                .get()
                .await()

            // If exists, return true
            documentSnapshot.exists()
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error checking course existence: ", e)
            false
        }
    }

    override suspend fun updateCourseTimestamp(userId: String, courseId: Int): Boolean {
        val timestampMap = mapOf(
            "timestamp" to Date()
        )

        return try {
            firestore.collection("users").document(userId)
                .collection("recent_courses").document(courseId.toString())
                .update(timestampMap)
                .await()
            Log.d("FIRESTORE", "Updated timestamp for course $courseId successfully")
            true
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error updating timestamp: ", e)
            false
        }
    }

    override suspend fun getRecentCourses(userId: String): List<Map<String, Any>> {
        return try {
            val querySnapshot = firestore.collection("users")
                .document(userId)
                .collection("recent_courses")
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            querySnapshot.documents.map { document ->
                document.data ?: emptyMap()
            }
        } catch (e: Exception) {
            Log.e("FIRESTORE ERROR", "Error fetching recent courses: ", e)
            emptyList()
        }
    }
}