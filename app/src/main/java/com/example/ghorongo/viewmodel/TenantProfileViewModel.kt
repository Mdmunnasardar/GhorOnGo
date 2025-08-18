package com.example.ghorongo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghorongo.data.model.Result
import com.example.ghorongo.data.model.Tenant
import com.example.ghorongo.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TenantProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var tenant by mutableStateOf<Tenant?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUpdating by mutableStateOf(false)

    init {
        loadTenantProfile()
    }

    fun loadTenantProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val userId = Firebase.auth.currentUser?.uid ?: return@launch
                when (val result = userRepository.getTenantProfile(userId)) {
                    is Result.Success -> tenant = result.data
                    is Result.Failure -> errorMessage = "Failed to load profile: ${result.exception.message}"
                }
            } catch (e: Exception) {
                errorMessage = "Error loading profile: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTenantProfile(updatedTenant: Tenant, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isUpdating = true
            errorMessage = null

            try {
                val userId = Firebase.auth.currentUser?.uid ?: return@launch
                when (val result = userRepository.updateTenantProfile(userId, updatedTenant)) {
                    is Result.Success -> {
                        tenant = updatedTenant
                        onSuccess()
                    }
                    is Result.Failure -> errorMessage = "Failed to update profile: ${result.exception.message}"
                }
            } catch (e: Exception) {
                errorMessage = "Error updating profile: ${e.message}"
            } finally {
                isUpdating = false
            }
        }
    }

    fun refreshProfile() {
        loadTenantProfile()
    }
}