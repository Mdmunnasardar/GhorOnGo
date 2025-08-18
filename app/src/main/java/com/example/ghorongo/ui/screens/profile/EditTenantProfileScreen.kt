package com.example.ghorongo.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ghorongo.data.model.Tenant
import com.example.ghorongo.ui.component.common.*
import com.example.ghorongo.ui.theme.PrimaryColor
import com.example.ghorongo.viewmodel.TenantProfileViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTenantProfileScreen(
    navController: NavController,
    viewModel: TenantProfileViewModel,
    tenant: Tenant
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Create editable state for all fields
    var editedTenant by remember { mutableStateOf(tenant) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Show error messages
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.errorMessage = null
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.updateTenantProfile(editedTenant) {
                                navController.popBackStack()
                            }
                        },
                        enabled = !viewModel.isUpdating
                    ) {
                        if (viewModel.isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Save, contentDescription = "Save")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Basic Information Section
            SectionHeader("Basic Information")
            OutlinedTextField(
                value = editedTenant.fullName,
                onValueChange = { editedTenant = editedTenant.copy(fullName = it) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )

            GenderSelection(
                selectedGender = editedTenant.gender,
                onGenderSelected = { editedTenant = editedTenant.copy(gender = it) }
            )

            DatePickerField(
                label = "Date of Birth",
                currentDate = editedTenant.dateOfBirth,
                onDateSelected = { editedTenant = editedTenant.copy(dateOfBirth = it) },
                formatter = dateFormatter
            )

            OutlinedTextField(
                value = editedTenant.permanentAddress,
                onValueChange = { editedTenant = editedTenant.copy(permanentAddress = it) },
                label = { Text("Permanent Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            OutlinedTextField(
                value = editedTenant.currentAddress,
                onValueChange = { editedTenant = editedTenant.copy(currentAddress = it) },
                label = { Text("Current Address") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 3
            )

            // Contact Information Section
            SectionHeader("Contact Information")
            OutlinedTextField(
                value = editedTenant.email,
                onValueChange = { editedTenant = editedTenant.copy(email = it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Email shouldn't be editable usually
            )

            OutlinedTextField(
                value = editedTenant.phoneNumber,
                onValueChange = { editedTenant = editedTenant.copy(phoneNumber = it) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedTenant.alternatePhoneNumber,
                onValueChange = { editedTenant = editedTenant.copy(alternatePhoneNumber = it) },
                label = { Text("Alternate Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            // Emergency Contact
            SectionHeader("Emergency Contact")
            OutlinedTextField(
                value = editedTenant.emergencyContactName,
                onValueChange = { editedTenant = editedTenant.copy(emergencyContactName = it) },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedTenant.emergencyContactRelation,
                onValueChange = { editedTenant = editedTenant.copy(emergencyContactRelation = it) },
                label = { Text("Relationship") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedTenant.emergencyContactPhone,
                onValueChange = { editedTenant = editedTenant.copy(emergencyContactPhone = it) },
                label = { Text("Phone") },
                modifier = Modifier.fillMaxWidth()
            )

            // Employment Information
            SectionHeader("Employment Information")
            OutlinedTextField(
                value = editedTenant.occupation,
                onValueChange = { editedTenant = editedTenant.copy(occupation = it) },
                label = { Text("Occupation") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedTenant.employer,
                onValueChange = { editedTenant = editedTenant.copy(employer = it) },
                label = { Text("Employer") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editedTenant.monthlyIncomeRange,
                onValueChange = { editedTenant = editedTenant.copy(monthlyIncomeRange = it) },
                label = { Text("Monthly Income Range") },
                modifier = Modifier.fillMaxWidth()
            )

            // Rental Preferences
            SectionHeader("Rental Preferences")
            OutlinedTextField(
                value = editedTenant.preferences.preferredLocation,
                onValueChange = { editedTenant = editedTenant.copy(
                    preferences = editedTenant.preferences.copy(preferredLocation = it)
                ) },
                label = { Text("Preferred Location") },
                modifier = Modifier.fillMaxWidth()
            )

            BudgetRangeSelector(
                minBudget = editedTenant.minBudget,
                maxBudget = editedTenant.maxBudget,
                onMinBudgetChanged = { editedTenant = editedTenant.copy(minBudget = it) },
                onMaxBudgetChanged = { editedTenant = editedTenant.copy(maxBudget = it) }
            )

            // Add more fields as needed...
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = PrimaryColor,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}