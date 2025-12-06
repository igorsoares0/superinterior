package com.example.superinterior.data.model

import androidx.annotation.DrawableRes

data class SavedDesign(
    val id: Int,
    @DrawableRes val imageRes: Int,
    val timestamp: Long = System.currentTimeMillis()
)
