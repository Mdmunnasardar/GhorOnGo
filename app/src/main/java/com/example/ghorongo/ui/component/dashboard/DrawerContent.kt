package com.example.ghorongo.ui.component.dashboard
import androidx.compose.material3.*

import androidx.compose.material.icons.filled.Person

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DrawerContent(onItemSelected: (String) -> Unit) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Navigation", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            DrawerMenuItem("Dashboard", Icons.Default.Home) { onItemSelected("dashboard") }
            DrawerMenuItem("Profile", Icons.Default.Person) { onItemSelected("profile") }
            DrawerMenuItem("Saved", Icons.Default.Favorite) { onItemSelected("saved") }
            DrawerMenuItem("Messages", Icons.Default.Email) { onItemSelected("message") }
        }
    }
}

@Composable
fun DrawerMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = label)
        Spacer(modifier = Modifier.width(16.dp))
        Text(label)
    }
}
