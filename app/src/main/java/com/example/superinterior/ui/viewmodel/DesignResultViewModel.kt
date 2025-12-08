package com.example.superinterior.ui.viewmodel

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

data class DesignResultUiState(
    val generatedImageUrl: String? = null,
    val processingTime: Float? = null,
    val isDownloading: Boolean = false,
    val downloadSuccess: Boolean = false,
    val downloadError: String? = null
)

class DesignResultViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DesignResultUiState())
    val uiState: StateFlow<DesignResultUiState> = _uiState.asStateFlow()

    fun setGeneratedImage(imageUrl: String?, processingTime: Float?) {
        _uiState.value = _uiState.value.copy(
            generatedImageUrl = imageUrl,
            processingTime = processingTime
        )
    }

    fun downloadImage(context: Context) {
        val imageUrl = _uiState.value.generatedImageUrl ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDownloading = true,
                downloadError = null,
                downloadSuccess = false
            )

            try {
                val bitmap = loadBitmapFromUrl(context, imageUrl)
                saveBitmapToGallery(context, bitmap)

                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    downloadSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDownloading = false,
                    downloadError = e.message ?: "Download failed"
                )
            }
        }
    }

    private suspend fun loadBitmapFromUrl(context: Context, url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val loader = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(url)
                .allowHardware(false) // Disable hardware bitmaps for saving
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            (result as BitmapDrawable).bitmap
        }
    }

    private suspend fun saveBitmapToGallery(context: Context, bitmap: Bitmap) {
        return withContext(Dispatchers.IO) {
            val fileName = "superinterior_${System.currentTimeMillis()}.jpg"

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ - use MediaStore
                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/SuperInterior")
                }

                val uri = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    contentValues
                )

                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                    }
                }
            } else {
                // Android 9 and below - use traditional file system
                val directory = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "SuperInterior"
                )

                if (!directory.exists()) {
                    directory.mkdirs()
                }

                val file = File(directory, fileName)
                FileOutputStream(file).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
                }

                // Trigger media scanner
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                intent.data = Uri.fromFile(file)
                context.sendBroadcast(intent)
            }
        }
    }

    fun clearDownloadStatus() {
        _uiState.value = _uiState.value.copy(
            downloadSuccess = false,
            downloadError = null
        )
    }
}
