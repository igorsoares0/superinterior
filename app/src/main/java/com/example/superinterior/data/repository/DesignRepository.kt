package com.example.superinterior.data.repository

import com.example.superinterior.data.local.dao.DesignDao
import com.example.superinterior.data.local.entities.DesignEntity
import com.example.superinterior.data.network.ApiService
import com.example.superinterior.data.network.models.GenerateResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

class DesignRepository(
    private val apiService: ApiService,
    private val designDao: DesignDao
) {

    suspend fun generateInteriorDesign(
        imageFile: File,
        style: String,
        roomType: String
    ): Result<GenerateResponse> = withContext(Dispatchers.IO) {
        apiService.redesignInterior(
            imageFile = imageFile,
            style = style,
            roomType = roomType
        )
    }

    suspend fun generateGardenDesign(
        imageFile: File,
        style: String,
        gardenType: String = "garden",
        strength: Float = 0.5f
    ): Result<GenerateResponse> = withContext(Dispatchers.IO) {
        apiService.gardenDesign(
            imageFile = imageFile,
            style = style,
            gardenType = gardenType,
            strength = strength
        )
    }

    suspend fun saveDesign(
        designType: String,
        style: String,
        roomType: String?,
        originalImagePath: String,
        generatedImageUrl: String,
        processingTime: Float
    ): Long = withContext(Dispatchers.IO) {
        val entity = DesignEntity(
            designType = designType,
            style = style,
            roomType = roomType,
            originalImagePath = originalImagePath,
            generatedImageUrl = generatedImageUrl,
            generatedImagePath = null,
            processingTime = processingTime
        )
        designDao.insertDesign(entity)
    }

    fun getAllDesigns(): Flow<List<DesignEntity>> {
        return designDao.getAllDesigns()
    }

    suspend fun deleteDesign(design: DesignEntity) = withContext(Dispatchers.IO) {
        designDao.deleteDesign(design)
    }
}
