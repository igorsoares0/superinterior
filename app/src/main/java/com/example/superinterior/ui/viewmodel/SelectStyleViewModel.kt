package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.superinterior.R
import com.example.superinterior.data.model.InteriorStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SelectStyleUiState(
    val styles: List<InteriorStyle> = emptyList(),
    val selectedStyleId: Int? = null,
    val currentStep: Int = 3,
    val totalSteps: Int = 3
)

class SelectStyleViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SelectStyleUiState())
    val uiState: StateFlow<SelectStyleUiState> = _uiState.asStateFlow()

    init {
        loadStyles()
    }

    private fun loadStyles() {
        _uiState.value = _uiState.value.copy(
            styles = listOf(
                InteriorStyle(
                    id = 1,
                    name = "Modern",
                    imageRes = R.drawable.modern
                ),
                InteriorStyle(
                    id = 2,
                    name = "Minimalist",
                    imageRes = R.drawable.minimalist
                ),
                InteriorStyle(
                    id = 3,
                    name = "Scandinavian",
                    imageRes = R.drawable.scandinavian
                ),
                InteriorStyle(
                    id = 4,
                    name = "Industrial",
                    imageRes = R.drawable.industrial
                )
            )
        )
    }

    fun selectStyle(styleId: Int) {
        _uiState.value = _uiState.value.copy(selectedStyleId = styleId)
    }

    fun onContinue() {
        // TODO: Processar e gerar design com IA
    }
}
