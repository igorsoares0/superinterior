package com.example.superinterior.ui.screens.addphoto

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun AddPhotoScreen(
    designType: String = "Interior Redesign",
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

    LaunchedEffect(designType) {
        viewModel.setDesignType(designType)
    }

    Scaffold(
        topBar = {
            StepProgressTopBar(
                currentStep = uiState.currentStep,
                totalSteps = uiState.actualTotalSteps,
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
                text = "Upload a photo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Upload area
            PhotoUploadArea(
                title = "Start Redesign",
                subtitle = "Redesign your room",
                selectedImageUri = uiState.selectedImageUri,
                onUploadClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error message
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

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
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = if (uiState.isProcessingImage) "Processing..." else "Continue",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
