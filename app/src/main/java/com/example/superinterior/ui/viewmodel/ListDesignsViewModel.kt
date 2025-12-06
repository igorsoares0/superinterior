package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.superinterior.R
import com.example.superinterior.data.model.SavedDesign
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ListDesignsUiState(
    val savedDesigns: List<SavedDesign> = emptyList()
)

class ListDesignsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ListDesignsUiState())
    val uiState: StateFlow<ListDesignsUiState> = _uiState.asStateFlow()

    init {
        loadSavedDesigns()
    }

    private fun loadSavedDesigns() {
        // TODO: Carregar designs do banco de dados (Room)
        // Por enquanto, usando dados mockados
        _uiState.value = _uiState.value.copy(
            savedDesigns = listOf(
                SavedDesign(id = 1, imageRes = R.drawable.redesign),
                SavedDesign(id = 2, imageRes = R.drawable.exterior),
                SavedDesign(id = 3, imageRes = R.drawable.redesign),
                SavedDesign(id = 4, imageRes = R.drawable.exterior),
                SavedDesign(id = 5, imageRes = R.drawable.redesign),
                SavedDesign(id = 6, imageRes = R.drawable.exterior)
            )
        )
    }

    fun onDesignClick(designId: Int) {
        // TODO: Navegar para tela de detalhes do design
    }
}
