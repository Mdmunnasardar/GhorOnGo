package com.example.ghorongo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ghorongo.data.model.Landlord
import com.example.ghorongo.data.repository.UserRepository
import com.example.ghorongo.ui.component.common.InfoItem
import com.example.ghorongo.ui.theme.PrimaryColor
import com.example.ghorongo.viewmodel.LandlordProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandlordProfileScreen(
    navController: NavController,
    viewModel: LandlordProfileViewModel = LandlordProfileViewModel(UserRepository())
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                title = { Text("My Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refreshProfile() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { /* Handle edit */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
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
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        } else {
            viewModel.landlord?.let { landlord ->
                LandlordProfileContent(
                    landlord = landlord,
                    modifier = Modifier.padding(innerPadding)
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No profile data found", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun LandlordProfileContent(
    landlord: Landlord,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeaderSection(landlord)
        }

        item {
            BasicInfoSection(landlord)
        }

        item {
            ContactInfoSection(landlord)
        }

        item {
            BusinessInfoSection(landlord)
        }

        item {
            VerificationStatusSection(landlord)
        }

        item {
            StatsSection(landlord)
        }
    }
}

@Composable
private fun ProfileHeaderSection(landlord: Landlord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                if (landlord.profilePicture.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(landlord.profilePicture)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, PrimaryColor, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile placeholder",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, PrimaryColor, CircleShape)
                            .background(Color.LightGray),
                        tint = Color.DarkGray
                    )
                }

                if (landlord.logo.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(landlord.logo)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Business logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "Business placeholder",
                            modifier = Modifier.size(24.dp),
                            tint = PrimaryColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = landlord.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (landlord.businessName.isNotEmpty()) {
                Text(
                    text = landlord.businessName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (landlord.isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Verified Landlord",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Green
                    )
                } else {
                    Text(
                        text = "Not Verified",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Red
                    )
                }
            }
        }
    }
}
@Composable
private fun BasicInfoSection(landlord: Landlord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Basic Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(label = "Gender", value = landlord.gender.ifEmpty { "Not specified" })
            InfoItem(label = "Date of Birth", value = landlord.dateOfBirth?.toString() ?: "Not specified")
            InfoItem(label = "Permanent Address", value = landlord.permanentAddress.ifEmpty { "Not specified" })
            InfoItem(label = "Property Address", value = landlord.propertyAddress.ifEmpty { "Not specified" })
        }
    }
}

@Composable
private fun ContactInfoSection(landlord: Landlord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Contact Information",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(label = "Email", value = landlord.email)
            InfoItem(label = "Phone", value = landlord.phoneNumber.ifEmpty { "Not specified" })

            if (landlord.emergencyContactName.isNotEmpty() || landlord.emergencyContactPhone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Emergency Contact",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                InfoItem(label = "Name", value = landlord.emergencyContactName.ifEmpty { "Not specified" })
                InfoItem(label = "Phone", value = landlord.emergencyContactPhone.ifEmpty { "Not specified" })
            }
        }
    }
}

@Composable
private fun BusinessInfoSection(landlord: Landlord) {
    if (landlord.businessName.isNotEmpty() || landlord.businessLicense.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Business Information",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoItem(label = "Business License", value = landlord.businessLicense.ifEmpty { "Not provided" })
            }
        }
    }
}

@Composable
private fun VerificationStatusSection(landlord: Landlord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Verification Status",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(
                label = "Government ID",
                value = if (landlord.govtIdProof.isNotEmpty()) "Provided (${landlord.govtIdNumber})" else "Not provided"
            )
        }
    }
}

@Composable
private fun StatsSection(landlord: Landlord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    value = landlord.totalRoomsListed.toString(),
                    label = "Listed Rooms"
                )

                if (landlord.roomTypes.isNotEmpty()) {
                    StatItem(
                        value = landlord.roomTypes.size.toString(),
                        label = "Room Types"
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PrimaryColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}