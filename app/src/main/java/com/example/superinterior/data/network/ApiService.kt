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

    /**
     * Exterior redesign - multipart/form-data
     */
    suspend fun designExterior(
        imageFile: File,
        style: String,
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
                .addFormDataPart("model", model)
                .build()

            val request = Request.Builder()
                .url("$baseUrl/api/design-exterior")
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
     * Reference style transfer - multipart/form-data (2 images)
     */
    suspend fun referenceStyle(
        baseImageFile: File,
        referenceImageFile: File,
        roomType: String,
        strength: Float = 0.6f,
        styleWeight: Float = 0.7f,
        model: String = "flux-dev"
    ): Result<GenerateResponse> {
        return try {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "base_image",
                    baseImageFile.name,
                    baseImageFile.asRequestBody("image/*".toMediaType())
                )
                .addFormDataPart(
                    "reference_image",
                    referenceImageFile.name,
                    referenceImageFile.asRequestBody("image/*".toMediaType())
                )
                .addFormDataPart("room_type", roomType)
                .addFormDataPart("strength", strength.toString())
                .addFormDataPart("style_weight", styleWeight.toString())
                .addFormDataPart("model", model)
                .build()

            val request = Request.Builder()
                .url("$baseUrl/api/reference-style")
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
