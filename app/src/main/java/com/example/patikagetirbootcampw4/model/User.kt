package com.example.patikagetirbootcampw4.model

data class User(
    val id: Int,
    val userId: String,
    val fullName: String,
    val email: String,
    val password: String,
    val phoneNumber: String?,
    val occupation: String?,
    val employer: String?,
    val country: String?,
    val latitude: Double,
    val longitude: Double
)
