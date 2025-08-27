package com.example.ghorongo.ui.component.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghorongo.data.model.Tenant

@Composable
fun TenantProfileContent(
    tenant: Tenant,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            ProfileHeaderSection(tenant)
        }

        item {
            BasicInfoSection(tenant)
        }

        item {
            ContactInfoSection(tenant)
        }

        item {
            EmploymentInfoSection(tenant)
        }

        item {
            PreferencesSection(tenant)
        }

        item {
            VerificationStatusSection(tenant)
        }

        if (tenant.rentalHistory.isNotEmpty()) {
            item {
                RentalHistorySection(tenant)
            }
        }
    }
}