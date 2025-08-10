package com.example.ghorongo.data.model


data class UserProfile(
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val userType: String = "",
    val nationalIdUrl: String? = null,
    val isVerified: Boolean = false
)