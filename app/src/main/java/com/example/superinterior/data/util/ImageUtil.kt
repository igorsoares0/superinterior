package com.example.superinterior.data.util

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ImageUtil {

    /**
     * Convert Uri to File for multipart upload
     * Copies image from Uri to app's cache directory
     */
    fun uriToFile(context: Context, uri: Uri, fileName: String = "upload_${System.currentTimeMillis()}.jpg"): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, fileName)

            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Validate image file size (max 10MB per API requirements)
     */
    fun isFileSizeValid(file: File, maxSizeInMB: Int = 10): Boolean {
        val fileSizeInMB = file.length() / (1024 * 1024)
        return fileSizeInMB <= maxSizeInMB
    }

    /**
     * Get human-readable file size
     */
    fun getFileSize(file: File): String {
        val bytes = file.length()
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> "${bytes / (1024 * 1024)} MB"
        }
    }
}
