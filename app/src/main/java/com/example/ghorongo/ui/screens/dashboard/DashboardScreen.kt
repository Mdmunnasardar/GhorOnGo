package com.example.ghorongo.ui.screens.dashboard

import LoadingIndicator
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ghorongo.ui.component.dashboard.DashboardUiState
import com.example.ghorongo.ui.component.search.SearchBar
import com.example.ghorongo.ui.component.text.SectionHeader
import com.example.ghorongo.ui.loading.EmptyState

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onRetry: () -> Unit,
    onFilterClick: () -> Unit,
    onRoomClick: (Int) -> Unit  // Add room click callback
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            query = state.query,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier.padding(top = 16.dp)
        )

        when {
            state.isLoading -> {
                LoadingIndicator()
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
                    SectionHeader(title = "Suggested Rooms")

                    // Placeholder for your rooms list (replace with your RoomCard if available)
                    state.rooms.forEach { room ->
                        Text(
                            text = room.name,  // assuming your room has a `name` property
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { onRoomClick(room.id) } // room click triggers navigation
                        )
                    }
                }
            }
        }
    }
}
