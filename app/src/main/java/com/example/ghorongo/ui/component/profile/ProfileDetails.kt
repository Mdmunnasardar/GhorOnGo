package com.example.ghorongo.ui.component.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ghorongo.data.model.Tenant
import com.example.ghorongo.ui.component.common.InfoItem
import com.example.ghorongo.ui.theme.PrimaryColor
import kotlin.text.ifEmpty

@Composable
fun EmploymentInfoSection(tenant: Tenant) {
    if (tenant.occupation.isNotEmpty() || tenant.employer.isNotEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Employment Information",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoItem(label = "Occupation", value = tenant.occupation.ifEmpty { "Not specified" })
                InfoItem(label = "Employer", value = tenant.employer.ifEmpty { "Not specified" })
            }
        }
    }
}

@Composable
fun PreferencesSection(tenant: Tenant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rental Preferences",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(label = "Preferred Location", value = tenant.preferences.preferredLocation.ifEmpty { "Not specified" })
            InfoItem(label = "Budget Range", value = tenant.preferences.budgetRange.ifEmpty { "Not specified" })
            InfoItem(label = "Preferred Room Type", value = tenant.preferences.roomType.ifEmpty { "Not specified" })

            if (tenant.preferences.amenities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Preferred Amenities",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = tenant.preferences.amenities.joinToString(", "),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun VerificationStatusSection(tenant: Tenant) {
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
                value = if (tenant.govtIdProof.isNotEmpty()) "Provided (${tenant.govtIdNumber})" else "Not provided"
            )
        }
    }
}

@Composable
fun RentalHistorySection(tenant: Tenant) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rental History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            tenant.rentalHistory.forEach { history ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = "Status: ${history.status.replaceFirstChar { it.uppercase() }}",
                            fontWeight = FontWeight.Bold,
                            color = when (history.status) {
                                "active" -> Color.Green
                                "completed" -> Color.Blue
                                else -> Color.Gray
                            }
                        )
                        InfoItem(label = "Period", value = "${history.startDate} - ${history.endDate}")
                        InfoItem(label = "Rent", value = "$${history.rentAmount}/month")
                    }
                }
            }
        }
    }
}