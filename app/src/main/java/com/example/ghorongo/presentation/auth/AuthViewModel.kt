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

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun signUp(navController: NavController) = viewModelScope.launch {
        try {
            if (password != confirmPassword) {
                errorMessage = "Passwords don't match"
                return@launch
            }

            isLoading = true
            errorMessage = null
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            successMessage = "Verification email sent. Please check your inbox."
            navController.popBackStack()
        } catch (e: Exception) {
            errorMessage = e.message ?: "Signup failed"
        } finally {
            isLoading = false
        }
    }

    fun login(navController: NavController) = viewModelScope.launch {
        try {
            isLoading = true
            errorMessage = null
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (!result.user?.isEmailVerified!!) {
                errorMessage = "Please verify your email first"
                auth.signOut()
                return@launch
            }
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: "Login failed"
        } finally {
            isLoading = false
        }
    }

    fun sendPasswordReset() = viewModelScope.launch {
        try {
            if (email.isBlank()) {
                errorMessage = "Please enter your email"
                return@launch
            }

            isLoading = true
            errorMessage = null
            auth.sendPasswordResetEmail(email).await()
            successMessage = "Password reset email sent to $email"
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to send reset email"
        } finally {
            isLoading = false
        }
    }
}