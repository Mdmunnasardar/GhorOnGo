package com.example.ghorongo.data.model

import java.util.Date

data class Tenant(
    val userId: String = "",
    val fullName: String = "",
    val profilePicture: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val dateOfBirth: Date? = null,
    val permanentAddress: String = "",
    val currentAddress: String = "",
    val govtIdProof: String = "",
    val govtIdNumber: String = "",
    val studentEmployeeId: String = "",
    val emergencyContactName: String = "",
    val emergencyContactRelation: String = "",
    val emergencyContactPhone: String = "",
    val alternatePhoneNumber: String = "",
    val isVerified: Boolean = false,
    val occupation: String = "",
    val instituteCompanyName: String = "",
    val courseJobPosition: String = "",
    val monthlyIncomeRange: String = "",
    val durationOfStay: String = "",
    val languagesKnown: List<String> = emptyList(),
    val livingSituation: String = "",
    val familyMembers: Int = 0,
    val relationshipDetails: String = "",
    val ageGroup: String = "",
    val friendsGroupMembers: Int = 0,
    val preferredRoomType: String = "",
    val minBudget: Double = 0.0,
    val maxBudget: Double = 0.0,
    val requiredAmenities: List<String> = emptyList(),
    val foodPreference: String = "",
    val hasPets: Boolean = false,
    val smokingHabit: Boolean = false,
    val drinkingHabit: Boolean = false,
    val employer: String = "",
    val rentalHistory: List<RentalHistory> = emptyList(),
    val preferences: TenantPreferences = TenantPreferences(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()

)

data class RentalHistory(
    val propertyId: String = "",
    val landlordId: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val rentAmount: Double = 0.0,
    val status: String = "" // "active", "completed", "terminated"
)

data class TenantPreferences(
    val preferredLocation: String = "",
    val budgetRange: String = "",
    val roomType: String = "",
    val amenities: List<String> = emptyList()
)
