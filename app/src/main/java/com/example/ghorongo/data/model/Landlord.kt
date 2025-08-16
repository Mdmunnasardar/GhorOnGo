package com.example.ghorongo.data.model

import java.util.Date

data class Landlord(
    val userId: String = "",
    val fullName: String = "",
    val businessName: String = "",
    val profilePicture: String = "",
    val logo: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val dateOfBirth: Date? = null,
    val permanentAddress: String = "",
    val propertyAddress: String = "",
    val govtIdProof: String = "",
    val govtIdNumber: String = "",
    val businessLicense: String = "",
    val emergencyContactName: String = "",
    val emergencyContactPhone: String = "",
    val isVerified: Boolean = false,
    val totalRoomsListed: Int = 0,
    val roomTypes: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)