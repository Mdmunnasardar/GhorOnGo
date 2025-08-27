package com.example.ghorongo.ui.component.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ghorongo.data.model.Tenant
import com.example.ghorongo.ui.component.common.InfoItem
import com.example.ghorongo.ui.theme.PrimaryColor
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.text.ifEmpty

@Composable
fun ProfileHeaderSection(tenant: Tenant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (tenant.profilePicture.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(tenant.profilePicture)
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

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = tenant.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            if (tenant.occupation.isNotEmpty()) {
                Text(
                    text = tenant.occupation,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (tenant.isVerified) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = Color.Green,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Verified Tenant",
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
fun BasicInfoSection(tenant: Tenant) {
    val dateFormatter = remember {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    }

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

            InfoItem(label = "Gender", value = tenant.gender.ifEmpty { "Not specified" })
            InfoItem(
                label = "Date of Birth",
                value = tenant.dateOfBirth?.let { dateFormatter.format(it) } ?: "Not specified"
            )
            InfoItem(label = "Permanent Address", value = tenant.permanentAddress.ifEmpty { "Not specified" })
            InfoItem(label = "Current Address", value = tenant.currentAddress.ifEmpty { "Not specified" })
        }
    }
}

@Composable
fun ContactInfoSection(tenant: Tenant) {
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

            InfoItem(label = "Email", value = tenant.email)
            InfoItem(label = "Phone", value = tenant.phoneNumber.ifEmpty { "Not specified" })

            if (tenant.emergencyContactName.isNotEmpty() || tenant.emergencyContactPhone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Emergency Contact",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                InfoItem(label = "Name", value = tenant.emergencyContactName.ifEmpty { "Not specified" })
                InfoItem(label = "Phone", value = tenant.emergencyContactPhone.ifEmpty { "Not specified" })
            }
        }
    }
}