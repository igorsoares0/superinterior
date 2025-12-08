package com.example.superinterior.data.local.dao

import androidx.room.*
import com.example.superinterior.data.local.entities.DesignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DesignDao {

    @Insert
    suspend fun insertDesign(design: DesignEntity): Long

    @Query("SELECT * FROM designs ORDER BY createdAt DESC")
    fun getAllDesigns(): Flow<List<DesignEntity>>

    @Query("SELECT * FROM designs WHERE id = :id")
    suspend fun getDesignById(id: Int): DesignEntity?

    @Query("SELECT * FROM designs WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteDesigns(): Flow<List<DesignEntity>>

    @Update
    suspend fun updateDesign(design: DesignEntity)

    @Delete
    suspend fun deleteDesign(design: DesignEntity)

    @Query("DELETE FROM designs")
    suspend fun deleteAllDesigns()
}
