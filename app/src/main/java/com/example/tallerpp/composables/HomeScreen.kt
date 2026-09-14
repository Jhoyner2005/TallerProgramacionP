package com.example.tallerpp.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    bestScore: Int,
    onClickGame: () -> Unit,
    onClickInstruction: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {

        // Clouds
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Left cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 28f,
                center = Offset(
                    size.width * 0.15f,
                    size.height * 0.12f
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 38f,
                center = Offset(
                    size.width * 0.22f,
                    size.height * 0.11f
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.9f),
                radius = 26f,
                center = Offset(
                    size.width * 0.29f,
                    size.height * 0.13f
                )
            )
            // Right Cloud
            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 26f,
                center = Offset(
                    size.width * 0.72f,
                    size.height * 0.09f
                )
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 36f,
                center = Offset(
                    size.width * 0.79f,
                    size.height * 0.08f
                )
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.85f),
                radius = 25f,
                center = Offset(
                    size.width * 0.86f,
                    size.height * 0.11f
                )
            )
        }

        // Golf course
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 190.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 70.dp,
                        topEnd = 70.dp
                    )
                )
                .background(Color(0xFF4CAF50))
        )

        // Flag
        Icon(
            imageVector = Icons.Default.Flag,
            contentDescription = "Golf flag",
            tint = Color(0xFFE53935),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 30.dp, y = (-80).dp)
                .padding(top = 200.dp)
                .size(100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Tittle
            Text(
                text = "GOLF",
                color = Color.White,
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "MASTER",
                color = Color(0xFFFFD54F),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            // statical Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.92f)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(28.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "BEST SCORE",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF555555)
                        )

                        Text(
                            text = if (bestScore == 0) "-" else "$bestScore",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF176B3A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Golf ball
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Main button
            Button(
                onClick = onClickGame,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107)
                )
            ) {

                Text(
                    text = "PLAY GOLF",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF214D2F)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Golf Game • 2026",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}