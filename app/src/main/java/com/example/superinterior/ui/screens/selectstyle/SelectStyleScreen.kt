package com.example.superinterior.ui.screens.selectstyle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superinterior.ui.components.BottomNavBar
import com.example.superinterior.ui.components.StepProgressTopBar
import com.example.superinterior.ui.components.StyleSelectionCard
import com.example.superinterior.ui.viewmodel.SelectStyleViewModel

@Composable
fun SelectStyleScreen(
    isGardenDesign: Boolean = false,
    onNavigateBack: () -> Unit,
    onContinue: () -> Unit,
    viewModel: SelectStyleViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBottomItem by remember { mutableStateOf(1) }

    val currentStep = if (isGardenDesign) 2 else 3
    val totalSteps = if (isGardenDesign) 2 else 3

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
                onClick = onContinue,
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
}
