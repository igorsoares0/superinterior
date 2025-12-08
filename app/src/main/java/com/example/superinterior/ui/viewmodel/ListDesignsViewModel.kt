package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superinterior.data.local.entities.DesignEntity
import com.example.superinterior.data.repository.DesignRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ListDesignsUiState(
    val savedDesigns: List<DesignEntity> = emptyList(),
    val isLoading: Boolean = false
)

class ListDesignsViewModel(
    private val repository: DesignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListDesignsUiState())
    val uiState: StateFlow<ListDesignsUiState> = _uiState.asStateFlow()

    init {
        loadSavedDesigns()
    }

    private fun loadSavedDesigns() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getAllDesigns().collect { designs ->
                _uiState.value = _uiState.value.copy(
                    savedDesigns = designs,
                    isLoading = false
                )
            }
        }
    }

    fun deleteDesign(design: DesignEntity) {
        viewModelScope.launch {
            repository.deleteDesign(design)
        }
    }

    fun onDesignClick(designId: Int) {
        // TODO: Navigate to design details
    }
}
