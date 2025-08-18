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
}
