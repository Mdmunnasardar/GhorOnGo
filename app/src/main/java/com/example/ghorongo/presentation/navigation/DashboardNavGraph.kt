package com.example.ghorongo.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.ghorongo.ui.component.dashboard.DashboardUiState
import com.example.ghorongo.ui.screens.dashboard.DashboardScreen
import androidx.compose.runtime.Composable
import com.example.ghorongo.ui.screens.dashboard.FiltersScreen
import com.example.ghorongo.ui.screens.dashboard.RoomDetailScreen

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController,
    dashboardUiState: DashboardUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit
) {
    composable("dashboard") {
        DashboardScreen(
            state = dashboardUiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            onRetry = onRetry,
            onFilterClick = { navController.navigate("filters") },
            onRoomClick = { roomId -> navController.navigate("room_detail/$roomId") }
        )
    }

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
}

