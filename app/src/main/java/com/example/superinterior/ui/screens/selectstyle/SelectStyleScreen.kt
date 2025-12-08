package com.example.superinterior.ui.screens.selectstyle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superinterior.ui.components.BottomNavBar
import com.example.superinterior.ui.components.StepProgressTopBar
import com.example.superinterior.ui.components.StyleSelectionCard
import com.example.superinterior.ui.viewmodel.ApiState
import com.example.superinterior.ui.viewmodel.DesignFlowViewModel
import com.example.superinterior.ui.viewmodel.SelectStyleViewModel

@Composable
fun SelectStyleScreen(
    isGardenDesign: Boolean = false,
    onNavigateBack: () -> Unit,
    onContinue: (String) -> Unit,
    viewModel: SelectStyleViewModel = viewModel(),
    designFlowViewModel: DesignFlowViewModel? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBottomItem by remember { mutableStateOf(1) }

    val currentStep = if (isGardenDesign) 2 else 3
    val totalSteps = if (isGardenDesign) 2 else 3

    val flowState by designFlowViewModel?.flowState?.collectAsState() ?: remember { mutableStateOf(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        topBar = {
            StepProgressTopBar(
                currentStep = currentStep,
                totalSteps = totalSteps,
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "Select a style",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Select a style to create your desired interior design.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Style grid (2x2)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // First row: Modern, Minimalist
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val modernStyle = uiState.styles.getOrNull(0)
                    if (modernStyle != null) {
                        StyleSelectionCard(
                            styleName = modernStyle.name,
                            imageRes = modernStyle.imageRes,
                            isSelected = uiState.selectedStyleId == modernStyle.id,
                            onClick = { viewModel.selectStyle(modernStyle.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    val minimalistStyle = uiState.styles.getOrNull(1)
                    if (minimalistStyle != null) {
                        StyleSelectionCard(
                            styleName = minimalistStyle.name,
                            imageRes = minimalistStyle.imageRes,
                            isSelected = uiState.selectedStyleId == minimalistStyle.id,
                            onClick = { viewModel.selectStyle(minimalistStyle.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Second row: Scandinavian, Industrial
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val scandinavianStyle = uiState.styles.getOrNull(2)
                    if (scandinavianStyle != null) {
                        StyleSelectionCard(
                            styleName = scandinavianStyle.name,
                            imageRes = scandinavianStyle.imageRes,
                            isSelected = uiState.selectedStyleId == scandinavianStyle.id,
                            onClick = { viewModel.selectStyle(scandinavianStyle.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    val industrialStyle = uiState.styles.getOrNull(3)
                    if (industrialStyle != null) {
                        StyleSelectionCard(
                            styleName = industrialStyle.name,
                            imageRes = industrialStyle.imageRes,
                            isSelected = uiState.selectedStyleId == industrialStyle.id,
                            onClick = { viewModel.selectStyle(industrialStyle.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue button
            Button(
                onClick = {
                    val selectedStyle = uiState.styles.find { it.id == uiState.selectedStyleId }
                    selectedStyle?.let {
                        onContinue(it.name.lowercase())
                    }
                },
                enabled = uiState.selectedStyleId != null,
                modifier = Modifier
                    .fillMaxWidth()
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

        // Loading overlay
        if (flowState?.generationState is ApiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 4.dp,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Generating design...",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This may take up to 30 seconds",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Error dialog
        if (flowState?.generationState is ApiState.Error) {
            val errorState = flowState?.generationState as ApiState.Error
            AlertDialog(
                onDismissRequest = { designFlowViewModel?.reset() },
                title = {
                    Text(
                        text = "Generation Failed",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(text = errorState.message)
                },
                confirmButton = {
                    Button(
                        onClick = { designFlowViewModel?.reset() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black
                        )
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            designFlowViewModel?.reset()
                            val selectedStyle = uiState.styles.find { it.id == uiState.selectedStyleId }
                            selectedStyle?.let {
                                designFlowViewModel?.setStyle(it.name.lowercase())
                                designFlowViewModel?.generateDesign()
                            }
                        }
                    ) {
                        Text("Retry", color = Color.Black)
                    }
                }
            )
        }
    }
}
