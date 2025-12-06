package com.example.superinterior.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddPhotoUiState(
    val selectedImageUri: Uri? = null,
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val designType: String = "Interior Redesign"
) {
    val isGardenDesign: Boolean
        get() = designType == "Garden Design"

    val actualTotalSteps: Int
        get() = if (isGardenDesign) 2 else 3
}

class AddPhotoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AddPhotoUiState())
    val uiState: StateFlow<AddPhotoUiState> = _uiState.asStateFlow()

    fun setDesignType(type: String) {
        _uiState.value = _uiState.value.copy(
            designType = type,
            totalSteps = if (type == "Garden Design") 2 else 3
        )
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.value = _uiState.value.copy(selectedImageUri = uri)
    }

    fun onContinue() {
        // TODO: Navegar para próximo passo
    }

    fun onUploadClick() {
        // TODO: Abrir seletor de foto
    }
}
