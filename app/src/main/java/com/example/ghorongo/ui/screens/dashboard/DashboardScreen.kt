package com.example.ghorongo.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ghorongo.ui.component.dashboard.DashboardUiState
import com.example.ghorongo.ui.component.search.SearchBar
import com.example.ghorongo.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    state: DashboardUiState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    Button(
                        onClick = { authViewModel.logout(navController) }
                    ) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Search Bar
            SearchBar(
                query = state.query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Suggested Rooms")
        }
    }
}
