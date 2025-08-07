package com.example.ghorongo.presentation.auth
import androidx.lifecycle.ViewModel
import com.example.ghorongo.ui.component.dashboard.DashboardUiState
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        // Add logic to update room list or filter
    }

    fun onSearch() {
        // Implement search logic (maybe fetch rooms based on query)
    }

    fun onRetry() {
        // Retry loading rooms logic
    }
}
