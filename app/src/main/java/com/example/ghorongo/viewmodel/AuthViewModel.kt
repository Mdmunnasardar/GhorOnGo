package com.example.ghorongo.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.ghorongo.data.model.Landlord
import com.example.ghorongo.data.model.Result
import com.example.ghorongo.data.model.Tenant
import com.example.ghorongo.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // User input states
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var userType by mutableStateOf("") // "landlord" or "tenant"

    // UI state
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun login(navController: NavController) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null

            if (email.isBlank() || password.isBlank()) {
                errorMessage = "Please fill in all fields"
                return@launch
            }

            auth.signInWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid ?: return@launch

            when (val result = userRepository.getUserType(userId)) {
                is Result.Success -> {
                    val destination =
                        if (result.data == "landlord") "profile" else "dashboard"
                    navController.navigate(destination) {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }

                is Result.Failure -> {
                    errorMessage = "Failed to get user type: ${result.exception.message}"
                }
            }
        } catch (e: Exception) {
            errorMessage = "Login failed: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun signUp(navController: NavController) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null

            when {
                fullName.isBlank() -> {
                    errorMessage = "Please enter your name"
                    return@launch
                }

                userType.isBlank() -> {
                    errorMessage = "Please select user type"
                    return@launch
                }

                password.length < 6 -> {
                    errorMessage = "Password needs 6+ characters"
                    return@launch
                }

                password != confirmPassword -> {
                    errorMessage = "Passwords don't match"
                    return@launch
                }
            }

            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            val userId = result.user?.uid ?: return@launch

            // Save basic user info to Firestore
            val userData = hashMapOf(
                "fullName" to fullName,
                "email" to email,
                "userType" to userType,
                "createdAt" to System.currentTimeMillis()
            )

            Firebase.firestore.collection("users").document(userId)
                .set(userData)
                .await()

            // Create tenant or landlord profile
            when (userType) {
                "tenant" -> {
                    val tenant = Tenant(
                        userId = userId,
                        fullName = fullName,
                        email = email
                    )
                    userRepository.createTenantProfile(tenant)
                }

                "landlord" -> {
                    val landlord = Landlord(
                        userId = userId,
                        fullName = fullName,
                        email = email
                    )
                    userRepository.createLandlordProfile(landlord)
                }
            }

            successMessage = "Account created! Please verify your email."
            navController.navigate("user_type_selection") {
                popUpTo("signup") { inclusive = true }
            }
        } catch (e: Exception) {
            errorMessage = "Signup failed: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    fun sendPasswordReset(
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null
            successMessage = null

            auth.sendPasswordResetEmail(email).await()
            successMessage = "Password reset link sent"

            onSuccess()
        } catch (e: Exception) {
            errorMessage = "Failed to send reset link: ${e.localizedMessage}"
            onFailure(e.localizedMessage ?: "Unknown error")
        } finally {
            isLoading = false
        }
    }
    fun logout(navController: NavController) {
        auth.signOut()
        navController.navigate("login") {
            popUpTo("dashboard") { inclusive = true }
            launchSingleTop = true
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }

    private fun clearForm() {
        fullName = ""
        email = ""
        password = ""
        confirmPassword = ""
        clearMessages()
    }
}

// ✅ Factory to create AuthViewModel with UserRepository
class AuthViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}