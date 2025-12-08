package com.example.superinterior.data.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenerateResponse(
    val success: Boolean,
    @SerialName("output_url") val outputUrl: String? = null,
    val style: String? = null,
    @SerialName("room_type") val roomType: String? = null,
    @SerialName("model_used") val modelUsed: String? = null,
    @SerialName("processing_time") val processingTime: Float? = null,
    val error: String? = null
)
