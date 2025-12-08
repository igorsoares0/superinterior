package com.example.superinterior.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superinterior.data.util.ImageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

data class AddPhotoUiState(
    val selectedImageUri: Uri? = null,
    val selectedImageFile: File? = null,
    val currentStep: Int = 1,
    val totalSteps: Int = 3,
    val designType: String = "Interior Redesign",
    val isProcessingImage: Boolean = false,
    val error: String? = null
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

    fun onPhotoSelected(context: Context, uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessingImage = true, error = null)

            val file = withContext(Dispatchers.IO) {
                ImageUtil.uriToFile(context, uri)
            }

            if (file != null && ImageUtil.isFileSizeValid(file)) {
                _uiState.value = _uiState.value.copy(
                    selectedImageUri = uri,
                    selectedImageFile = file,
                    isProcessingImage = false,
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isProcessingImage = false,
                    error = if (file == null) "Failed to load image" else "Image too large (max 10MB)"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
