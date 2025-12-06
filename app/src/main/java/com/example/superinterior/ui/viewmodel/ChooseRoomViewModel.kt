package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChooseRoomUiState(
    val roomTypes: List<String> = listOf(
        "Kitchen",
        "Living room",
        "Bathroom",
        "Office",
        "Attic",
        "Dining room",
        "Room"
    ),
    val selectedRoom: String? = null,
    val currentStep: Int = 2,
    val totalSteps: Int = 3
)

class ChooseRoomViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChooseRoomUiState())
    val uiState: StateFlow<ChooseRoomUiState> = _uiState.asStateFlow()

    fun selectRoom(roomType: String) {
        _uiState.value = _uiState.value.copy(selectedRoom = roomType)
    }

    fun onContinue() {
        // TODO: Navegar para próximo passo
    }
}
