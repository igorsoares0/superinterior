package com.example.superinterior.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.superinterior.R
import com.example.superinterior.data.model.DesignStyle
import com.example.superinterior.data.model.DesignType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _designStyles = MutableStateFlow<List<DesignStyle>>(emptyList())
    val designStyles: StateFlow<List<DesignStyle>> = _designStyles.asStateFlow()

    init {
        loadDesignStyles()
    }

    private fun loadDesignStyles() {
        _designStyles.value = listOf(
            DesignStyle(
                id = 1,
                title = "Interior Redesign",
                description = "Upload a photo, choose a style, and let AI design the room.",
                imageRes = R.drawable.redesign,
                type = DesignType.INTERIOR
            ),
            DesignStyle(
                id = 2,
                title = "Exterior Redesign",
                description = "Upload a photo, choose a style, and let AI design the exterior.",
                imageRes = R.drawable.exterior,
                type = DesignType.EXTERIOR
            ),
            DesignStyle(
                id = 3,
                title = "Garden Design",
                description = "Upload a photo, choose a style, and let AI design the garden design.",
                imageRes = R.drawable.garden,
                type = DesignType.GARDEN
            ),
            DesignStyle(
                id = 4,
                title = "Reference Style",
                description = "Upload a photo, choose a image reference, and let AI design the room.",
                imageRes = R.drawable.reference,
                type = DesignType.REFERENCE
            )
        )
    }

    fun onStyleSelected(styleId: Int) {
        // TODO: Implementar navega\u00e7\u00e3o para a tela de design
    }
}
