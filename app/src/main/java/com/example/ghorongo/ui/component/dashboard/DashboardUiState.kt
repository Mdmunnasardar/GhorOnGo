package com.example.ghorongo.ui.component.dashboard

import com.example.ghorongo.data.model.Room

data class DashboardUiState(
    val query: String = "",                    // For the search bar
    val isLoading: Boolean = false,            // Show loading spinner
    val rooms: List<Room> = emptyList(),       // List of room data (empty = show EmptyState)
    val isError: Boolean = false               // Optional: for showing error state or retry logic
)