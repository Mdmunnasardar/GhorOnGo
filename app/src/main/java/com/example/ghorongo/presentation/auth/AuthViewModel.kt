package com.example.ghorongo.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // User input states
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    // UI state
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // AuthViewModel.kt
    fun login(navController: NavController) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null

            // Basic check for empty fields
            if (email.isBlank() || password.isBlank()) {
                errorMessage = "Please fill in all fields"
                return@launch
            }

            val result = auth.signInWithEmailAndPassword(email, password).await()

            // Navigate to home screen on success
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } catch (e: Exception) {
            errorMessage = "Login failed: ${e.message}"
            // Don't crash, just show error
        } finally {
            isLoading = false
        }
    }

    fun signUp(navController: NavController) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null

            // Basic field checks
            when {
                fullName.isBlank() -> {
                    errorMessage = "Please enter your name"
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

            // Show success and navigate back
            successMessage = "Account created! Please verify your email."
            navController.popBackStack()
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