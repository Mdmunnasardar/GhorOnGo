package com.example.ghorongo.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ghorongo.R
import com.example.ghorongo.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisible by remember { mutableStateOf(false) }

    // Toast state
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    LaunchedEffect(authViewModel.errorMessage, authViewModel.successMessage) {
        authViewModel.errorMessage?.let {
            toastMessage = it
            showToast = true
            delay(3000)
            showToast = false
            authViewModel.errorMessage = null
        }
        authViewModel.successMessage?.let {
            toastMessage = it
            showToast = true
            delay(3000)
            showToast = false
            authViewModel.successMessage = null
        }
    }

    val emailTrimmed = authViewModel.email.trim()
    val passwordTrimmed = authViewModel.password
    val isLoginEnabled = emailTrimmed.isNotEmpty() && passwordTrimmed.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A90E2), Color(0xFF1E3A8A))
                )
            )
    ) {
        if (showToast) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = Color.Black.copy(alpha = 0.7f)
            ) {
                Text(
                    text = toastMessage,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(60.dp))
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Login to continue your journey",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text("Email", color = Color.White.copy(alpha = 0.8f)) },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = Color.White) },
                colors = TextFieldDefaults.run {
                    outlinedTextFieldColors(
                                unfocusedTextColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedBorderColor = Color.White,
                                cursorColor = Color.White
                            )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(androidx.compose.ui.focus.FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = { Text("Password", color = Color.White.copy(alpha = 0.8f)) },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.White) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                colors = TextFieldDefaults.run {
                    outlinedTextFieldColors(
                                unfocusedTextColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                focusedBorderColor = Color.White,
                                cursorColor = Color.White
                            )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (isLoginEnabled) {
                            authViewModel.login(navController, emailTrimmed, passwordTrimmed)
                        } else {
                            authViewModel.errorMessage = "Please fill in all fields"
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button (light blue)
            Button(
                onClick = {
                    keyboardController?.hide()
                    if (isLoginEnabled) {
                        authViewModel.login(navController, emailTrimmed, passwordTrimmed)
                    } else {
                        authViewModel.errorMessage = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                enabled = isLoginEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLoginEnabled) Color(0xFF1E88E5) else Color.Gray
                )
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Login", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Forgot Password (center below login button)
            TextButton(
                onClick = { navController.navigate("forgot_password") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Forgot Password?", color = Color.White.copy(alpha = 0.9f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up
            Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("Don't have an account?", color = Color.White.copy(alpha = 0.8f))
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Sign Up", color = Color.Yellow)
                }
            }
        }
    }
}
