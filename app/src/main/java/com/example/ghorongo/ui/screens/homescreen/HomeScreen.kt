package com.example.ghorongo.ui.screens.homescreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.ghorongo.presentation.auth.DashboardViewModel
import com.example.ghorongo.presentation.navigation.dashboardNavGraph

@Composable
fun HomeScreen(viewModel: DashboardViewModel = DashboardViewModel()) {
    val navController = rememberNavController()

    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        dashboardNavGraph(
            navController = navController,
            dashboardUiState = uiState,
            onQueryChange = viewModel::onQueryChange,
            onSearch = viewModel::onSearch,
            onRetry = viewModel::onRetry
        )
    }
}
