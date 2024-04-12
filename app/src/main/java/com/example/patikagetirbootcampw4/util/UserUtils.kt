package com.example.patikagetirbootcampw4.util

import com.example.patikagetirbootcampw4.model.User
import com.google.gson.Gson

class UserUtils {
    fun parseUserJson(jsonData: String): User {
        return Gson().fromJson(jsonData, User::class.java)
    }
}
