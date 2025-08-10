package com.example.ghorongo.presentation.navigation


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home

import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Home", Icons.Default.Home)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object Saved : Screen("saved", "Saved", Icons.Default.Bookmark)
    // Use the AutoMirrored version of the Message icon
    object Message : Screen("message", "Messages", Icons.AutoMirrored.Filled.Message)
}
