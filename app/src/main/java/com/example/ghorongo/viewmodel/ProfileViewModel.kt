package com.example.ghorongo.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val name: String = "",
    val address: String = "",
    val phone: String = "",
    val email: String = "",
    val userType: String = "",
    val nationalIdUrl: String? = null,
    val isVerified: Boolean = false
)

class ProfileViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) : ViewModel() {

    private val userId = auth.currentUser?.uid

    // State holders
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error.asSharedFlow()

    init {
        userId?.let { loadUserProfile() }
    }

    fun loadUserProfile() {
        if (userId == null) {
            emitError("User not authenticated")
            return
        }
        viewModelScope.launch {
            runCatching {
                _loading.value = true
                firestore.collection("users").document(userId).get().await()
            }.onSuccess { doc ->
                val userProfile = doc.toObject(UserProfile::class.java)
                if (userProfile != null) {
                    _profile.value = userProfile.copy(email = auth.currentUser?.email.orEmpty())
                } else {
                    emitError("Profile not found")
                }
            }.onFailure { ex ->
                emitError("Failed to load profile: ${ex.localizedMessage}")
            }.also {
                _loading.value = false
            }
        }
    }

    fun updateProfile(updated: UserProfile) {
        if (userId == null) {
            emitError("User not authenticated")
            return
        }
        viewModelScope.launch {
            runCatching {
                _loading.value = true
                firestore.collection("users").document(userId).set(updated).await()
            }.onSuccess {
                _profile.value = updated
            }.onFailure { ex ->
                emitError("Update failed: ${ex.localizedMessage}")
            }.also {
                _loading.value = false
            }
        }
    }

    fun uploadNationalIdImage(imageUri: Uri) {
        if (userId == null) {
            emitError("User not authenticated")
            return
        }
        viewModelScope.launch {
            runCatching {
                _loading.value = true
                val imageRef = storage.reference.child("national_ids/$userId/${System.currentTimeMillis()}.jpg")
                imageRef.putFile(imageUri).await()
                imageRef.downloadUrl.await().toString()
            }.onSuccess { downloadUrl ->
                val updatedProfile = _profile.value?.copy(
                    nationalIdUrl = downloadUrl,
                    isVerified = false
                ) ?: UserProfile(
                    email = auth.currentUser?.email.orEmpty(),
                    userType = "owner",
                    nationalIdUrl = downloadUrl,
                    isVerified = false
                )
                updateProfile(updatedProfile)
            }.onFailure { ex ->
                emitError("Upload failed: ${ex.localizedMessage}")
                _loading.value = false
            }
        }
    }

    // FIXED: emitError now safely emits from a coroutine scope
    private fun emitError(message: String) {
        viewModelScope.launch {
            _error.emit(message)
        }
    }
}
