package com.example.superinterior.data.model

import androidx.annotation.DrawableRes

data class InteriorStyle(
    val id: Int,
    val name: String,
    @DrawableRes val imageRes: Int
)
