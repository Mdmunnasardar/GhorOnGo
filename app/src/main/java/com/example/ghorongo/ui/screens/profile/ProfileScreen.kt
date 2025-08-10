package com.example.ghorongo.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.ghorongo.presentation.profile.ProfileViewModel
import com.example.ghorongo.presentation.profile.UserProfile
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val profile by profileViewModel.profile.collectAsState()
    val loading by profileViewModel.loading.collectAsState()

    // Local state for form fields
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Collect errors from ViewModel and show as Snackbars
    LaunchedEffect(Unit) {
        profileViewModel.error.collectLatest { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
        }
    }

    // Populate form fields when profile loads or changes
    LaunchedEffect(profile) {
        profile?.let {
            name = it.name
            address = it.address
            phone = it.phone
            email = it.email
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileViewModel.uploadNationalIdImage(it) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = {},
                label = { Text("Email") },
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            profile?.let {
                if (it.userType == "owner") {
                    Text("National ID Verification", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!it.nationalIdUrl.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(it.nationalIdUrl),
                            contentDescription = "National ID",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Text(
                            text = if (it.isVerified) "Verified" else "Pending Verification",
                            color = if (it.isVerified)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        Text("No national ID uploaded")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { imagePicker.launch("image/*") }) {
                        Text("Upload National ID")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    profileViewModel.updateProfile(
                        UserProfile(
                            name = name,
                            address = address,
                            phone = phone,
                            email = email,
                            userType = profile?.userType ?: "",
                            nationalIdUrl = profile?.nationalIdUrl,
                            isVerified = profile?.isVerified ?: false
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading
            ) {
                Text("Save Changes")
            }
        }
    }
}
