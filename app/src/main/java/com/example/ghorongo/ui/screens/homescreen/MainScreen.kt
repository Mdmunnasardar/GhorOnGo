package com.example.ghorongo.ui.screens.homescreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ghorongo.ui.navigation.BottomNavItem
import com.example.ghorongo.ui.navigation.BottomNavigationBar
import com.example.ghorongo.ui.screens.booking.SavedScreen
import com.example.ghorongo.ui.screens.chat.MessageScreen
import com.example.ghorongo.ui.screens.dashboard.RoomDetailScreen
import com.example.ghorongo.ui.screens.profile.ProfileScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Define bottom navigation items with icons
    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", "dashboard", Icons.Default.Home),
        BottomNavItem("Profile", "profile", Icons.Default.Person),
        BottomNavItem("Saved", "saved", Icons.Default.Bookmark),
        BottomNavItem("Message", "message", Icons.AutoMirrored.Filled.Message)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, items = bottomNavItems)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                HomeScreen(navController = navController)
            }
            composable("profile") { ProfileScreen() }
            composable("saved") { SavedScreen() }
            composable("message") { MessageScreen() }

            composable(
                "room_detail/{roomId}",
                arguments = listOf(navArgument("roomId") { type = NavType.IntType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: -1
                RoomDetailScreen(roomId = roomId)
            }
        }
    }
}