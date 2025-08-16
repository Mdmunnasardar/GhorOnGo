package com.example.ghorongo.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ghorongo.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1976D2)) // Blue background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Join our community",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f)),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Full Name Field
            OutlinedTextField(
                value = authViewModel.fullName,
                onValueChange = { authViewModel.fullName = it },
                label = { Text("Full Name", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                    focusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            // Email Field
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text("Email", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                    focusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            // Password Field
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = { Text("Password", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                    focusedLabelColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true
            )

            // Confirm Password Field
            OutlinedTextField(
                value = authViewModel.confirmPassword,
                onValueChange = { authViewModel.confirmPassword = it },
                label = { Text("Confirm Password", color = Color.White) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                    focusedLabelColor = Color.White
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "I am a:",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = authViewModel.userType == "tenant",
                        onClick = { authViewModel.userType = "tenant" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "Tenant",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    RadioButton(
                        selected = authViewModel.userType == "landlord",
                        onClick = { authViewModel.userType = "landlord" },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "Landlord",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            // Sign Up Button
            // SignUpScreen.kt
            Button(
                onClick = {
                    when {
                        authViewModel.fullName.isBlank() -> {
                            authViewModel.errorMessage = "Please enter your name"
                        }
                        authViewModel.password.length < 6 -> {
                            authViewModel.errorMessage = "Password needs 6+ characters"
                        }
                        authViewModel.password != authViewModel.confirmPassword -> {
                            authViewModel.errorMessage = "Passwords don't match"
                        }
                        else -> {
                            authViewModel.signUp(navController)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !authViewModel.isLoading
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Sign Up")
                }
            }

            // Login Prompt
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Already have an account? ", color = Color.White)
                Text(
                    "Login",
                    color = Color(0xFFFFD700) // Gold color for emphasis
                )
            }

            // Error Message
            authViewModel.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color(0xFFFF5252), // Light red
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Success Message
            authViewModel.successMessage?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFF69F0AE), // Light green
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}