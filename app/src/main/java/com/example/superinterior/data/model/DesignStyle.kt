package com.example.superinterior.data.model

import androidx.annotation.DrawableRes

data class DesignStyle(
    val id: Int,
    val title: String,
    val description: String,
    @DrawableRes val imageRes: Int,
    val type: DesignType
)

enum class DesignType {
    INTERIOR,
    EXTERIOR,
    GARDEN,
    REFERENCE
}
