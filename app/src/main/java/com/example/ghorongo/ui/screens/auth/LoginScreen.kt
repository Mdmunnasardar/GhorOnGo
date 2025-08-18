package com.example.ghorongo.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    var passwordVisible by remember { mutableStateOf(false) }

    // Auto-suggestions state
    val emailSuggestions = remember { listOf("user@example.com", "admin@example.com") }
    var showEmailSuggestions by remember { mutableStateOf(false) }

    // Toast state
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    // Show error as toast
    LaunchedEffect(authViewModel.errorMessage) {
        authViewModel.errorMessage?.let {
            toastMessage = it
            showToast = true
            delay(3000)
            showToast = false
            authViewModel.errorMessage = null
        }
    }

    // Show success as toast
    LaunchedEffect(authViewModel.successMessage) {
        authViewModel.successMessage?.let {
            toastMessage = it
            showToast = true
            delay(3000)
            showToast = false
            authViewModel.successMessage = null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.blue),
                        Color(0xFF1E3A8A)
                    )
                )
            )
    ) {
        // Toast message
        if (showToast) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shadow(8.dp),
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
            // App Logo with modern shadow
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(60.dp),
                        spotColor = Color.White.copy(alpha = 0.3f)
                    )
                    .padding(bottom = 24.dp)
            )

            // Title with modern typography
            Text(
                text = "Welcome Back",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
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

            // Email Field with suggestions
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = authViewModel.email,
                    onValueChange = {
                        authViewModel.email = it
                        showEmailSuggestions = it.isNotEmpty()
                    },
                    label = {
                        Text("Email",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedTextColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                        focusedBorderColor = Color.White,
                        cursorColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    singleLine = true
                )

                // Email suggestions dropdown
                if (showEmailSuggestions && emailSuggestions.any { it.contains(authViewModel.email, ignoreCase = true) }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                            .background(Color.White.copy(alpha = 0.1f)),
                        color = Color.Transparent
                    ) {
                        Column {
                            emailSuggestions
                                .filter { it.contains(authViewModel.email, ignoreCase = true) }
                                .take(3)
                                .forEach { suggestion ->
                                    Text(
                                        text = suggestion,
                                        color = Color.White,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                            .clickable {
                                                authViewModel.email = suggestion
                                                showEmailSuggestions = false
                                                focusManager.moveFocus(FocusDirection.Down)
                                            }
                                    )
                                }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Password Field with visibility toggle
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it },
                label = {
                    Text("Password",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = Color.White.copy(alpha = 0.8f)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password"
                            else "Show password",
                            tint = Color.White.copy(alpha = 0.8f)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedBorderColor = Color.White,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (authViewModel.email.isNotBlank() && authViewModel.password.isNotBlank()) {
                            authViewModel.login(navController)
                        } else {
                            authViewModel.errorMessage = "Please fill in all fields"
                        }
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            // Login Button with gradient
            Button(
                onClick = {
                    if (authViewModel.email.isNotBlank() && authViewModel.password.isNotBlank()) {
                        authViewModel.login(navController)
                    } else {
                        authViewModel.errorMessage = "Please fill in all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                enabled = !authViewModel.isLoading
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4A90E2),
                                    Color(0xFF1E3A8A)
                                )
                            )
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (authViewModel.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Login",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            // Forgot Password
            TextButton(
                onClick = { navController.navigate("forgot_password") },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    "Forgot Password?",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            // Divider
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
                Text(
                    text = "OR",
                    color = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 12.dp),
                    style = MaterialTheme.typography.labelSmall
                )
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.3f))
                )
            }

            // Sign Up
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Don't have an account? ",
                    color = Color.White.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = { navController.navigate("signup") },
                    modifier = Modifier.padding(start = 0.dp)
                ) {
                    Text(
                        "Sign Up",
                        color = Color(0xFFFFD700),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}