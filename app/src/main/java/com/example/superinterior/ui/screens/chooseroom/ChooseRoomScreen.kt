package com.example.superinterior.ui.screens.chooseroom

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
import com.example.superinterior.ui.components.RoomTypeButton
import com.example.superinterior.ui.components.StepProgressTopBar
import com.example.superinterior.ui.viewmodel.ChooseRoomViewModel

@Composable
fun ChooseRoomScreen(
    onNavigateBack: () -> Unit,
    onContinue: (String) -> Unit,
    viewModel: ChooseRoomViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBottomItem by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            StepProgressTopBar(
                currentStep = uiState.currentStep,
                totalSteps = uiState.totalSteps,
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
                text = "Choose a room",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Choose a room to design and watch it transform into the style you've chosen.",
                fontSize = 14.sp,
                color = Color.Gray,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Room buttons grid
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // First row: Kitchen, Living room
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoomTypeButton(
                        roomName = "Kitchen",
                        isSelected = uiState.selectedRoom == "Kitchen",
                        onClick = { viewModel.selectRoom("Kitchen") },
                        modifier = Modifier.weight(1f)
                    )
                    RoomTypeButton(
                        roomName = "Living room",
                        isSelected = uiState.selectedRoom == "Living room",
                        onClick = { viewModel.selectRoom("Living room") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Second row: Bathroom, Office
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoomTypeButton(
                        roomName = "Bathroom",
                        isSelected = uiState.selectedRoom == "Bathroom",
                        onClick = { viewModel.selectRoom("Bathroom") },
                        modifier = Modifier.weight(1f)
                    )
                    RoomTypeButton(
                        roomName = "Office",
                        isSelected = uiState.selectedRoom == "Office",
                        onClick = { viewModel.selectRoom("Office") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Third row: Attic, Dining room
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoomTypeButton(
                        roomName = "Attic",
                        isSelected = uiState.selectedRoom == "Attic",
                        onClick = { viewModel.selectRoom("Attic") },
                        modifier = Modifier.weight(1f)
                    )
                    RoomTypeButton(
                        roomName = "Dining room",
                        isSelected = uiState.selectedRoom == "Dining room",
                        onClick = { viewModel.selectRoom("Dining room") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Fourth row: Room (alone)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RoomTypeButton(
                        roomName = "Room",
                        isSelected = uiState.selectedRoom == "Room",
                        onClick = { viewModel.selectRoom("Room") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue button
            Button(
                onClick = {
                    uiState.selectedRoom?.let { room ->
                        onContinue(room.lowercase().replace(" ", "_"))
                    }
                },
                enabled = uiState.selectedRoom != null,
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
