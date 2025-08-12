package com.example.ghorongo.presentation.auth
import androidx.lifecycle.ViewModel
import com.example.ghorongo.data.model.Room
import com.example.ghorongo.ui.component.dashboard.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
class DashboardViewModel : ViewModel() {

    private val sampleRooms = listOf(
        Room(1, "Living Room"),
        Room(2, "Dining Room"),
        Room(3, "Bedroom"),
        Room(4, "Kitchen"),
        Room(5, "Guest Room")
    )

    private val _uiState = MutableStateFlow(
        DashboardUiState(rooms = sampleRooms)
    )
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
    }

    fun onSearch(query: String) {
        val filteredRooms = sampleRooms.filter { it.name.contains(query, ignoreCase = true) }
        _uiState.value = _uiState.value.copy(rooms = filteredRooms)
    }

    fun onRetry() {
        _uiState.value = _uiState.value.copy(rooms = sampleRooms)
    }
}
