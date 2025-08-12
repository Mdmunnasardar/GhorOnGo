package com.example.ghorongo.ui.screens.homescreen

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ghorongo.presentation.auth.DashboardViewModel
import com.example.ghorongo.presentation.navigation.BottomNavItem
import com.example.ghorongo.ui.component.dashboard.DrawerContent
import com.example.ghorongo.ui.screens.booking.SavedScreen
import com.example.ghorongo.ui.screens.chat.MessageScreen
import com.example.ghorongo.ui.screens.dashboard.DashboardScreen
import com.example.ghorongo.ui.screens.dashboard.RoomDetailScreen
import com.example.ghorongo.ui.screens.profile.ProfileScreen
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewModel: DashboardViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Define bottom navigation items with icons
    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", "dashboard", Icons.Default.Home),
        BottomNavItem("Profile", "profile", Icons.Default.Person),
        BottomNavItem("Saved", "saved", Icons.Default.Bookmark),
        BottomNavItem("Message", "message", Icons.AutoMirrored.Filled.Message)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(onItemSelected = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
                scope.launch { drawerState.close() }
            })
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("My App") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Avoid multiple copies of the same destination
                                    launchSingleTop = true
                                    // Restore state when reselecting a tab
                                    restoreState = true
                                    // Pop up to the start destination of the graph to avoid building up back stack
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.name) },
                            label = { Text(item.name) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "dashboard",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("dashboard") {
                    DashboardScreen(
                        state = uiState,
                        onQueryChange = viewModel::onQueryChange,
                        onSearch = viewModel::onSearch,
                        onRetry = viewModel::onRetry,
                        onFilterClick = { navController.navigate("filters") },
                        onRoomClick = { roomId -> navController.navigate("room_detail/$roomId") }
                    )
                }
                composable("profile") { ProfileScreen() }
                composable("saved") { SavedScreen() }
                composable("message") { MessageScreen() }

                // **Add this:**
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
}