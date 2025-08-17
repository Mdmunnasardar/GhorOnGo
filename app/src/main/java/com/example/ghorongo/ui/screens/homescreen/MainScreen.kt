package com.example.ghorongo.ui.screens.homescreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ghorongo.ui.navigation.BottomNavItem
import com.example.ghorongo.ui.navigation.BottomNavigationBar
import com.example.ghorongo.ui.screens.booking.SavedScreen
import com.example.ghorongo.ui.screens.chat.MessageScreen
import com.example.ghorongo.ui.screens.dashboard.DashboardScreen
import com.example.ghorongo.ui.screens.profile.ProfileScreen
import com.example.ghorongo.viewmodel.AuthViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startRoute: String
) {
    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", "dashboard", Icons.Default.Home),
        BottomNavItem("Profile", "profile", Icons.Default.Person),
        BottomNavItem("Saved", "saved", Icons.Default.Bookmark),
        BottomNavItem("Message", "message", Icons.AutoMirrored.Filled.Message)
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = bottomNavItems) }
    ) { innerPadding ->
        when (startRoute) {
            "dashboard" -> DashboardScreen(
                navController = navController,
                authViewModel = authViewModel,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
            "profile" -> ProfileScreen()
            "saved" -> SavedScreen()
            "message" -> MessageScreen()
        }
    }
}
