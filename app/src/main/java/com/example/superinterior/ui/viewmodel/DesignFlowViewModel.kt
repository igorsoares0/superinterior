package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.superinterior.data.network.models.GenerateResponse
import com.example.superinterior.data.repository.DesignRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

data class DesignFlowState(
    val designType: String = "Interior Redesign",
    val imageFile: File? = null,
    val originalImagePath: String? = null,
    val referenceImageFile: File? = null,
    val referenceImagePath: String? = null,
    val roomType: String? = null,
    val selectedStyle: String? = null,
    val generationState: ApiState<GenerateResponse> = ApiState.Idle
)

/**
 * Orchestrates the entire design generation flow.
 * Holds state that needs to be shared across multiple screens.
 */
class DesignFlowViewModel(
    private val repository: DesignRepository
) : ViewModel() {

    private val _flowState = MutableStateFlow(DesignFlowState())
    val flowState: StateFlow<DesignFlowState> = _flowState.asStateFlow()

    fun setDesignType(designType: String) {
        _flowState.value = _flowState.value.copy(designType = designType)
    }

    fun setImageFile(file: File, path: String) {
        _flowState.value = _flowState.value.copy(
            imageFile = file,
            originalImagePath = path
        )
    }

    fun setReferenceImageFile(file: File, path: String) {
        _flowState.value = _flowState.value.copy(
            referenceImageFile = file,
            referenceImagePath = path
        )
    }

    fun setRoomType(roomType: String) {
        _flowState.value = _flowState.value.copy(roomType = roomType)
    }

    fun setStyle(style: String) {
        _flowState.value = _flowState.value.copy(selectedStyle = style)
    }

    fun generateDesign() {
        val state = _flowState.value
        val imageFile = state.imageFile ?: return

        viewModelScope.launch {
            _flowState.value = _flowState.value.copy(
                generationState = ApiState.Loading
            )

            val result = when (state.designType) {
                "Interior Redesign" -> {
                    val style = state.selectedStyle ?: return@launch
                    val roomType = state.roomType ?: return@launch
                    repository.generateInteriorDesign(
                        imageFile = imageFile,
                        style = style,
                        roomType = roomType
                    )
                }
                "Garden Design" -> {
                    val style = state.selectedStyle ?: return@launch
                    repository.generateGardenDesign(
                        imageFile = imageFile,
                        style = style,
                        gardenType = "garden",
                        strength = 0.5f
                    )
                }
                "Exterior Redesign" -> {
                    val style = state.selectedStyle ?: return@launch
                    repository.generateExteriorDesign(
                        imageFile = imageFile,
                        style = style
                    )
                }
                "Reference Style" -> {
                    val referenceImageFile = state.referenceImageFile ?: return@launch
                    val roomType = state.roomType ?: return@launch
                    repository.generateReferenceStyle(
                        baseImageFile = imageFile,
                        referenceImageFile = referenceImageFile,
                        roomType = roomType,
                        strength = 0.6f,
                        styleWeight = 0.7f
                    )
                }
                else -> {
                    _flowState.value = _flowState.value.copy(
                        generationState = ApiState.Error("Unsupported design type: ${state.designType}")
                    )
                    return@launch
                }
            }

            result.fold(
                onSuccess = { response ->
                    if (response.success && response.outputUrl != null) {
                        // Save to database
                        val styleToSave = if (state.designType == "Reference Style") {
                            "reference_style"
                        } else {
                            state.selectedStyle ?: "unknown"
                        }

                        repository.saveDesign(
                            designType = state.designType,
                            style = styleToSave,
                            roomType = state.roomType,
                            originalImagePath = state.originalImagePath ?: "",
                            generatedImageUrl = response.outputUrl,
                            processingTime = response.processingTime ?: 0f
                        )

                        _flowState.value = _flowState.value.copy(
                            generationState = ApiState.Success(response)
                        )
                    } else {
                        _flowState.value = _flowState.value.copy(
                            generationState = ApiState.Error(response.error ?: "Unknown error")
                        )
                    }
                },
                onFailure = { error ->
                    _flowState.value = _flowState.value.copy(
                        generationState = ApiState.Error(
                            message = error.message ?: "Network error",
                            throwable = error
                        )
                    )
                }
            )
        }
    }

    fun reset() {
        _flowState.value = DesignFlowState()
    }
}
