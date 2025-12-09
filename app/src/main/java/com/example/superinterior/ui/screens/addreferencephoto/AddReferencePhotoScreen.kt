package com.example.superinterior.ui.screens.addreferencephoto

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superinterior.ui.components.BottomNavBar
import com.example.superinterior.ui.components.PhotoUploadArea
import com.example.superinterior.ui.components.StepProgressTopBar
import com.example.superinterior.ui.viewmodel.AddPhotoViewModel
import java.io.File

@Composable
fun AddReferencePhotoScreen(
    onNavigateBack: () -> Unit,
    onContinue: (File, String) -> Unit = { _, _ -> },
    viewModel: AddPhotoViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var selectedBottomItem by remember { mutableStateOf(1) }

    // Photo picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.onPhotoSelected(context, it) }
    }

    Scaffold(
        topBar = {
            StepProgressTopBar(
                currentStep = 2,
                totalSteps = 2,
                onCloseClick = onNavigateBack
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedBottomItem,
                onItemSelected = { selectedBottomItem = it }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Add a reference photo.",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Upload area
            PhotoUploadArea(
                title = "Add a photo to use",
                subtitle = "as a reference.",
                selectedImageUri = uiState.selectedImageUri,
                onUploadClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.weight(1f),
                buttonText = "Upload a reference"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Continue button
            Button(
                onClick = {
                    uiState.selectedImageFile?.let { file ->
                        onContinue(file, uiState.selectedImageUri.toString())
                    }
                },
                enabled = uiState.selectedImageFile != null && !uiState.isProcessingImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
