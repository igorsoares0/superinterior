package com.example.superinterior.data.network

import com.example.superinterior.data.network.models.GenerateResponse
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class ApiService(private val client: OkHttpClient = ApiClient.okHttpClient) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val baseUrl = ApiClient.BASE_URL

    /**
     * Redesign interior - multipart/form-data
     */
    suspend fun redesignInterior(
        imageFile: File,
        style: String,
        roomType: String,
        model: String = "flux-dev"
    ): Result<GenerateResponse> {
        return try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaType())
                )
                .addFormDataPart("style", style)
                .addFormDataPart("room_type", roomType)
                .addFormDataPart("model", model)
                .build()

            val request = Request.Builder()
                .url("$baseUrl/api/redesign-interior")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw IOException("Empty response")

            val result = json.decodeFromString<GenerateResponse>(responseBody)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Garden design - multipart/form-data
     */
    suspend fun gardenDesign(
        imageFile: File,
        style: String,
        gardenType: String = "garden",
        strength: Float = 0.5f,
        model: String = "flux-dev"
    ): Result<GenerateResponse> {
        return try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "image",
                    imageFile.name,
                    imageFile.asRequestBody("image/*".toMediaType())
                )
                .addFormDataPart("style", style)
                .addFormDataPart("garden_type", gardenType)
                .addFormDataPart("strength", strength.toString())
                .addFormDataPart("model", model)
                .build()

            val request = Request.Builder()
                .url("$baseUrl/api/garden-design")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: throw IOException("Empty response")

            val result = json.decodeFromString<GenerateResponse>(responseBody)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
