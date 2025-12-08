package com.example.superinterior.ui.components

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun PhotoUploadArea(
    title: String,
    subtitle: String,
    selectedImageUri: Uri? = null,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonText: String = "Upload a photo"
) {
    if (selectedImageUri != null) {
        // Show selected image preview
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(selectedImageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = "Selected image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onUploadClick) {
                Text(
                    text = "Change Photo",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }
    } else {
        // Show upload placeholder
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(520.dp),
            contentAlignment = Alignment.Center
        ) {
            // Dashed border box
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                drawRoundRect(
                    color = Color(0xFFCCCCCC),
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(12f, 12f),
                            phase = 0f
                        )
                    ),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onUploadClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    ),
                    shape = RoundedCornerShape(28.dp),
                    contentPadding = PaddingValues(horizontal = 36.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = buttonText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
