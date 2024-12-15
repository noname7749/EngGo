package com.example.enggo.model
import com.google.firebase.Timestamp

import com.example.enggo.utils.generateId

data class UserData(
    var username: String,
    var password: String,
    var email: String,
    var phone: String? = null,
    var id: String = generateId(username),
    var profile: String = id,
    var fcmToken: String? = null,
    var loginStreak: Int = 0,
    var lastLoginDate: Timestamp? = null
)