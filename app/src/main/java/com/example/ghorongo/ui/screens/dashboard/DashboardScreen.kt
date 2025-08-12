package com.example.ghorongo.ui.screens.dashboard

import LoadingIndicator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghorongo.ui.component.dashboard.DashboardUiState

import com.example.ghorongo.ui.component.text.SectionHeader
import com.example.ghorongo.ui.component.loading.EmptyState
import com.example.ghorongo.ui.component.search.SearchBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onRetry: () -> Unit,
    onFilterClick: () -> Unit,
    onRoomClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = state.query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when {
            state.isLoading -> {
                LoadingIndicator(modifier = Modifier.fillMaxSize())
            }
            state.rooms.isEmpty() -> {
                EmptyState(
                    title = "No Rooms Found",
                    subtitle = "Try adjusting your search or filters.",
                    icon = Icons.Outlined.HomeWork,
                    actionText = "Retry",
                    onActionClick = onRetry,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Suggested Rooms", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    state.rooms.forEach { room ->
                        Text(
                            text = room.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onRoomClick(room.id) }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
