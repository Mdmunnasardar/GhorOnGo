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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ghorongo.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1976D2))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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

            // Full Name
            // Full Name Field
            OutlinedTextField(
                value = authViewModel.fullName,
                onValueChange = { authViewModel.fullName = it.trimStart() },
                label = { Text("Full Name", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next // NEXT action on keyboard
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) } // move focus to next field
                )
            )

            // Email
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it.trimStart() },
                label = { Text("Email", color = Color.White) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                singleLine = true
            )

            // Password
            OutlinedTextField(
                value = authViewModel.password,
                onValueChange = { authViewModel.password = it.trimStart() },
                label = { Text("Password", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                singleLine = true
            )

            // Confirm Password
            OutlinedTextField(
                value = authViewModel.confirmPassword,
                onValueChange = { authViewModel.confirmPassword = it.trimStart() },
                label = { Text("Confirm Password", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (authViewModel.fullName.isNotBlank() &&
                            authViewModel.email.isNotBlank() &&
                            authViewModel.password.isNotBlank() &&
                            authViewModel.password == authViewModel.confirmPassword &&
                            authViewModel.userType.value.isNotBlank()
                        ) {
                            authViewModel.signUp(navController)
                        } else {
                            authViewModel.errorMessage = "Please fill all fields correctly"
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                singleLine = true
            )

            // User Type Selection
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = authViewModel.userType.value == "tenant",
                    onClick = { authViewModel.updateUserType("tenant") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                )
                Text("Tenant", color = Color.White, modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = authViewModel.userType.value == "landlord",
                    onClick = { authViewModel.updateUserType("landlord") },
                    colors = RadioButtonDefaults.colors(selectedColor = Color.White)
                )
                Text("Landlord", color = Color.White, modifier = Modifier.padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Button with active/inactive color
            val isFormValid = authViewModel.fullName.isNotBlank() &&
                    authViewModel.email.isNotBlank() &&
                    authViewModel.password.isNotBlank() &&
                    authViewModel.password == authViewModel.confirmPassword &&
                    authViewModel.userType.value.isNotBlank()

            Button(
                onClick = {
                    focusManager.clearFocus()
                    authViewModel.signUp(navController)
                },
                enabled = isFormValid && !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF4B13AF) else Color.Gray
                )
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Sign Up", color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login prompt
            TextButton(onClick = { navController.popBackStack() }) {
                Text("Already have an account? ", color = Color.White)
                Text("Login", color = Color(0xFF462285))
            }

            // Error & success messages
            authViewModel.errorMessage?.let {
                Text(it, color = Color(0xFFFF5252), modifier = Modifier.padding(top = 16.dp))
            }
            authViewModel.successMessage?.let {
                Text(it, color = Color(0xFF69F0AE), modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}
