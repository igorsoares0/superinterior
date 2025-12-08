package com.example.superinterior.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "designs")
data class DesignEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val designType: String,           // "Interior Redesign", "Exterior", etc.
    val style: String,                // "modern", "minimalist", etc.
    val roomType: String?,            // "living_room", "bedroom", etc.
    val originalImagePath: String,    // Caminho da imagem original
    val generatedImageUrl: String,    // URL da imagem gerada pela API
    val generatedImagePath: String?,  // Caminho local (quando baixada)
    val processingTime: Float,        // Tempo de geração
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
