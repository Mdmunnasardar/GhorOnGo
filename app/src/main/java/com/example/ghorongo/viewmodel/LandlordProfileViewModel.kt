package com.example.ghorongo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ghorongo.data.model.Landlord
import com.example.ghorongo.data.model.Result
import com.example.ghorongo.data.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LandlordProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    var landlord by mutableStateOf<Landlord?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadLandlordProfile()
    }

    private fun loadLandlordProfile() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val userId = Firebase.auth.currentUser?.uid ?: return@launch
                when (val result = userRepository.getLandlordProfile(userId)) {
                    is Result.Success -> {
                        landlord = result.data
                    }
                    is Result.Failure -> {
                        errorMessage = "Failed to load profile: ${result.exception.message}"
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error loading profile: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun refreshProfile() {
        loadLandlordProfile()
    }
}