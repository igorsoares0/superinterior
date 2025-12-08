package com.example.superinterior.ui.screens.listdesigns

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.superinterior.ui.components.BottomNavBar
import com.example.superinterior.ui.components.SavedDesignCard
import com.example.superinterior.ui.viewmodel.ListDesignsViewModel

@Composable
fun ListDesignsScreen(
    onNavigateToHome: () -> Unit = {},
    onNavigateToAddPhoto: (String) -> Unit = {},
    viewModel: ListDesignsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedBottomItem by remember { mutableStateOf(2) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedItem = selectedBottomItem,
                onItemSelected = { index ->
                    selectedBottomItem = index
                    when (index) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToAddPhoto("Interior Redesign")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top bar com PRO badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF2D5F4E)
                ) {
                    Text(
                        text = "PRO",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = "Your designs",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Grid de designs salvos
            if (uiState.savedDesigns.isEmpty()) {
                // Estado vazio
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Text(
                        text = "No designs yet",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.savedDesigns) { design ->
                        SavedDesignCard(
                            imageUrl = design.generatedImageUrl,
                            onClick = { viewModel.onDesignClick(design.id) }
                        )
                    }
                }
            }
        }
    }
}
