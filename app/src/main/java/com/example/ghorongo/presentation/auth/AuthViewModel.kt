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
import java.util.regex.Pattern

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    // User input states
    var fullName by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var emailError by mutableStateOf<String?>(null)

    // UI state
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // Email validation pattern
    private val emailPattern = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    )

    fun validateEmail(): Boolean {
        return when {
            email.isBlank() -> {
                emailError = "Email is required"
                false
            }
            !emailPattern.matcher(email).matches() -> {
                emailError = "Enter a valid email address"
                false
            }
            else -> {
                emailError = null
                true
            }
        }
    }

    fun signUp(navController: NavController) = viewModelScope.launch {
        try {
            when {
                fullName.isBlank() -> {
                    errorMessage = "Please enter your full name"
                    return@launch
                }
                !validateEmail() -> return@launch
                password.isBlank() -> {
                    errorMessage = "Please enter a password"
                    return@launch
                }
                password != confirmPassword -> {
                    errorMessage = "Passwords don't match"
                    return@launch
                }
                password.length < 6 -> {
                    errorMessage = "Password should be at least 6 characters"
                    return@launch
                }
            }

            isLoading = true
            errorMessage = null
            successMessage = null

            val result = auth.createUserWithEmailAndPassword(email, password).await()

            result.user?.let { user ->
                user.sendEmailVerification().await()
                successMessage = "Verification email sent to ${user.email}. Please verify your email."
                clearForm()
                navController.popBackStack()
            }

        } catch (e: Exception) {
            errorMessage = when (e.message) {
                "The email address is already in use by another account." -> "Email already registered"
                "The email address is badly formatted." -> "Invalid email format"
                else -> e.message ?: "Signup failed"
            }
        } finally {
            isLoading = false
        }
    }

    fun login(navController: NavController) = viewModelScope.launch {
        try {
            if (!validateEmail()) return@launch
            if (password.isBlank()) {
                errorMessage = "Please enter your password"
                return@launch
            }

            isLoading = true
            errorMessage = null
            successMessage = null

            val result = auth.signInWithEmailAndPassword(email, password).await()

            if (!result.user?.isEmailVerified!!) {
                errorMessage = "Please verify your email first. Check your inbox."
                auth.signOut()
                return@launch
            }

            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }

        } catch (e: Exception) {
            errorMessage = when (e.message) {
                "The password is invalid or the user does not have a password." -> "Invalid password"
                "There is no user record corresponding to this identifier." -> "Email not registered"
                else -> e.message ?: "Login failed"
            }
        } finally {
            isLoading = false
        }
    }

    fun sendPasswordReset(
        onSuccess: () -> Unit = {},
        onFailure: (String) -> Unit = {}
    ) = viewModelScope.launch {
        try {
            if (!validateEmail()) return@launch

            isLoading = true
            errorMessage = null
            successMessage = null

            auth.sendPasswordResetEmail(email).await()

            successMessage = "Password reset link sent to $email"
            onSuccess()

        } catch (e: Exception) {
            val msg = when (e.message) {
                "There is no user record corresponding to this identifier." -> "Email not registered"
                else -> e.message ?: "Failed to send reset email"
            }
            errorMessage = msg
            onFailure(msg)
        } finally {
            isLoading = false
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
        emailError = null
    }

    private fun clearForm() {
        fullName = ""
        email = ""
        password = ""
        confirmPassword = ""
        clearMessages()
    }
}
