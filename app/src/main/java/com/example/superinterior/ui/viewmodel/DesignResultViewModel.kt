package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.superinterior.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DesignResultUiState(
    val generatedImageRes: Int = R.drawable.final_design,
    val isLoading: Boolean = false
)

class DesignResultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DesignResultUiState())
    val uiState: StateFlow<DesignResultUiState> = _uiState.asStateFlow()

    fun onDownloadClick() {
        // TODO: Implementar download da imagem
    }
}
