package com.example.ghorongo.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ghorongo.Home
import com.example.ghorongo.presentation.auth.DashboardViewModel
import com.example.ghorongo.presentation.navigation.BottomNavBar
import com.example.ghorongo.presentation.navigation.BottomNavItem
import com.example.ghorongo.ui.screens.booking.SavedScreen
import com.example.ghorongo.ui.screens.chat.MessageScreen
import com.example.ghorongo.ui.screens.dashboard.DashboardScreen
import com.example.ghorongo.ui.screens.dashboard.FiltersScreen
import com.example.ghorongo.ui.screens.dashboard.RoomDetailScreen
import com.example.ghorongo.ui.screens.profile.ProfileScreen

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun HomeScreen() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", "dashboard", Icons.Default.Home),
        BottomNavItem("Profile", "profile", Icons.Default.Person),
        BottomNavItem("Saved", "saved", Icons.Default.Favorite),
        BottomNavItem("Messages", "message", Icons.Default.Email)
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, items = bottomNavItems)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            // Dashboard main screen + nested routes
            composable("dashboard") {
                val viewModel = DashboardViewModel()
                val uiState by viewModel.uiState.collectAsState()

                DashboardScreen(
                    state = uiState,
                    onQueryChange = viewModel::onQueryChange,
                    onSearch = viewModel::onSearch,
                    onRetry = viewModel::onRetry,
                    onFilterClick = { navController.navigate("filters") },
                    onRoomClick = { roomId -> navController.navigate("room_detail/$roomId") }
                )
            }

            // Dashboard sub screens
            composable("filters") {
                FiltersScreen(onBack = { navController.popBackStack() })
            }
            composable(
                "room_detail/{roomId}",
                arguments = listOf(navArgument("roomId") { type = NavType.IntType })
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getInt("roomId") ?: -1
                RoomDetailScreen(roomId = roomId)
            }

            // Other bottom nav screens
            composable("profile") { ProfileScreen() }
            composable("saved") { SavedScreen() }
            composable("message") { MessageScreen() }
        }
    }
}

