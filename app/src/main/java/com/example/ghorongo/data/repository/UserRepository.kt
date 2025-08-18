package com.example.ghorongo.data.repository

import com.example.ghorongo.data.model.Landlord
import com.example.ghorongo.data.model.Result
import com.example.ghorongo.data.model.Tenant
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun createTenantProfile(tenant: Tenant): Result<Unit> {
        return try {
            db.collection("tenants").document(tenant.userId)
                .set(tenant)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun createLandlordProfile(landlord: Landlord): Result<Unit> {
        return try {
            db.collection("landlords").document(landlord.userId)
                .set(landlord)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun getUserType(userId: String): Result<String> {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            val userType = snapshot.getString("userType") ?: "tenant"
            Result.Success(userType)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getLandlordProfile(userId: String): Result<Landlord> {
        return try {
            val snapshot = db.collection("landlords").document(userId).get().await()
            val landlord = snapshot.toObject(Landlord::class.java) ?: throw Exception("Profile not found")
            Result.Success(landlord)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getTenantProfile(userId: String): Result<Tenant> {
        return try {
            val snapshot = db.collection("tenants").document(userId).get().await()
            val tenant = snapshot.toObject(Tenant::class.java) ?: throw Exception("Profile not found")
            Result.Success(tenant)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun updateTenantProfile(userId: String, tenant: Tenant): Result<Unit> {
        return try {
            // Convert Date to Timestamp if needed
            val data = hashMapOf<String, Any>().apply {
                putAll(tenant.toMap()) // We'll add this extension function
            }

            db.collection("tenants").document(userId)
                .update(data)
                .await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    // Extension function to convert Tenant to Map (add this to your model file)
    fun Tenant.toMap(): Map<String, Any> {
        return mapOf(
            "fullName" to fullName,
            "profilePicture" to profilePicture,
            "phoneNumber" to phoneNumber,
            "email" to email,
            "gender" to gender,
            "dateOfBirth" to (dateOfBirth ?: ""),
            "permanentAddress" to permanentAddress,
            "currentAddress" to currentAddress,
            "govtIdProof" to govtIdProof,
            "govtIdNumber" to govtIdNumber,
            // Include all other fields...
            "updatedAt" to System.currentTimeMillis()
        )
    }
}
